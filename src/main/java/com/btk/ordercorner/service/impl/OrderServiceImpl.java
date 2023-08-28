package com.btk.ordercorner.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.btk.ordercorner.exception.customer.CustomerNotFoundException;
import com.btk.ordercorner.exception.customer.WalletNotEnoughException;
import com.btk.ordercorner.model.dto.OrderDetailsDto;
import com.btk.ordercorner.model.dto.OrderDto;
import com.btk.ordercorner.model.entity.CartProducts;
import com.btk.ordercorner.model.entity.Customer;
import com.btk.ordercorner.model.entity.Order;
import com.btk.ordercorner.model.entity.Product;
import com.btk.ordercorner.model.entity.ShoppingCart;
import com.btk.ordercorner.model.vm.AddOrderVm;
import com.btk.ordercorner.model.vm.GetOrderDetailsVm;
import com.btk.ordercorner.model.vm.GetOrdersVm;
import com.btk.ordercorner.model.vm.SearchOrdersByDateVm;
import com.btk.ordercorner.repository.CartProductsRepository;
import com.btk.ordercorner.repository.CustomerRepository;
import com.btk.ordercorner.repository.OrderRepository;
import com.btk.ordercorner.repository.ProductRepository;
import com.btk.ordercorner.repository.ShoppingCartRepository;
import com.btk.ordercorner.service.OrderService;
import com.btk.ordercorner.util.mapper.ModelMapperManager;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    
    private OrderRepository orderRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private ModelMapperManager modelMapperManager;
    private CartProductsServiceImpl cartProductsServiceImpl;
    private CartProductsRepository cartProductsRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapperManager modelMapperManager, 
    ShoppingCartRepository shoppingCartRepository, CartProductsServiceImpl cartProductsServiceImpl, 
    CartProductsRepository cartProductsRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.modelMapperManager = modelMapperManager;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartProductsServiceImpl = cartProductsServiceImpl;
        this.cartProductsRepository = cartProductsRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<GetOrdersVm> getAllOrderVm() {
        List<OrderDto> orderDtos = orderRepository.findAll().stream()
        .map(order -> modelMapperManager.forResponse().map(order, OrderDto.class)).collect(Collectors.toList());
        
        return orderDtos.stream()
                .map(orderDto -> modelMapperManager.forResponse().map(orderDto, GetOrdersVm.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int addOrderVm(AddOrderVm addOrderVm) {
        Authentication auth = checkAuth(addOrderVm.getCustomerId());
        long minDay = LocalDate.of(2010, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2022, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        Date orderDate = Date.valueOf(randomDate);
        addOrderVm.setOrderDate(orderDate);

        OrderDto orderDto = modelMapperManager.forResponse().map(addOrderVm, OrderDto.class);
        Order order = modelMapperManager.forResponse().map(orderDto, Order.class);
        ShoppingCart shoppingCart = shoppingCartRepository.getShoppingCartByCustomerId(addOrderVm.getCustomerId());
        
        //
        List<CartProducts> products = cartProductsRepository.getCartProductsByCustomerId(addOrderVm.getCustomerId());
        for (CartProducts cartProduct : products) {
            Product product = cartProduct.getProduct();
            int prevStockAmount = product.getStockAmount();
            int prevQuantity = cartProductsServiceImpl.getProductQuantityByCartId(addOrderVm.getCartId(), product.getProductId());
            int newQuantity = product.getStockAmount() - prevQuantity;
            product.setStockAmount(newQuantity); 
            logger.info("Sipariş sonrası ürün stok durumları güncellendi\n Ürün İsmi: " + product.getProductName() + "\n" + "Eski Stok Durumu: " + prevStockAmount +"\n" + "Yeni Stok Durumu: " + newQuantity);
            productRepository.save(product);
        }
        Customer customer = customerRepository.findById(addOrderVm.getCustomerId())
            .orElseThrow(() -> new CustomerNotFoundException(addOrderVm.getCustomerId() + " ID numarasına sahip bir kullanıcı bulunmamaktadır!"));

        boolean walletCheck=customer.getWallet() < shoppingCart.getTotalAmount();
        
        if(walletCheck){
            
            double requiredCost =shoppingCart.getTotalAmount() - customer.getWallet();
            logger.info(customer.getUsername() + " yetersiz bakiyeden dolayı satın alma işlemini gerçekleştiremedi.");
            throw new WalletNotEnoughException("Yetersiz Bakiye ----------- "+ requiredCost + " Tl cüzdanına eklemen gerekiyor." );

        }
        
        double newWallet = customer.getWallet() - shoppingCart.getTotalAmount();
        customer.setWallet(newWallet);
        customerRepository.save(customer);
        order.setTotalAmount(shoppingCart.getTotalAmount());
        order.setOrderStatus(true);
        logger.info("Sipariş oluşturuldu!");
        logger.info(auth.getName() + " adlı kullanıcının siparişinden sonra kalan bakiyesi: " + newWallet);
        shoppingCartRepository.findById(shoppingCart.getCartId()).get().setSoftDeleted(false);
        logger.info("Sipariş sonrası sepet güncellendi!");
        orderRepository.save(order);

        return order.getOrderId();
    
    }

    @Override
    public List<GetOrdersVm> getOrderByCustomerId(int customerId) {
        Authentication auth = checkAuth(customerId);
        List<Order> orders = orderRepository.findAll().stream().filter(order -> order.getCustomer().getCustomerId() == customerId).toList();
    
        logger.info(auth.getName() + " isimli kullanıcı tüm siparişlerini görüntüledi!");
        return orders.stream()
            .map(order -> modelMapperManager.forResponse().map(order, GetOrdersVm.class)).collect(Collectors.toList());
    }

    @Override
    public List<GetOrdersVm> getOrdersBeforeDate(SearchOrdersByDateVm orderVm) {
        Authentication auth = checkAuth(orderVm.getCustomerId());
        Date startDate = Date.valueOf(orderVm.getDate());
        List<Order> orders =  orderRepository.findByOrderDateBefore(startDate, orderVm.getCustomerId());
        logger.info(auth.getName() + " adlı kullanıcı " + orderVm.getDate() + " tarihinden önceki siparişlerini görüntüledi!");
        return orders.stream().
            map(order -> modelMapperManager.forResponse().map(order, GetOrdersVm.class)).collect(Collectors.toList());
    }

    private Authentication checkAuth(int customerId) {
        Authentication auth = getAuth();
        String username = customerRepository.findById(customerId).get().getUsername();
        if(!auth.getName().equals(username)) {
            String errorMessage = "Başka kullanıcının sipariş bilgilerine erişim hakkınız yoktur!";
            logger.error(errorMessage);
            throw new AccessDeniedException(errorMessage);
        }
        logger.info("Kullanıcı ile şifresi uyuşmaktadır!" + "\n" + username + " isimli kullanıcı doğrulandı");
        return auth;
    }

    public Authentication getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }

    @Override
    public List<GetOrdersVm> getOrdersAfterDate(SearchOrdersByDateVm orderVm) {
        Authentication auth = checkAuth(orderVm.getCustomerId());
        Date startDate = Date.valueOf(orderVm.getDate());
        List<Order> orders =  orderRepository.findByOrderDateAfter(startDate, orderVm.getCustomerId());
        logger.info(auth.getName() + " adlı kullanıcı " + orderVm.getDate() + " tarihinden sonraki siparişlerini görüntüledi!");
        return orders.stream().
            map(order -> modelMapperManager.forResponse().map(order, GetOrdersVm.class)).collect(Collectors.toList());
    }

    @Override
    public List<GetOrderDetailsVm> getOrderDetailsById(int orderId) {
        int cartId = orderRepository.findById(orderId).get().getCart().getCartId();
        int customerId = orderRepository.findById(orderId).get().getCustomer().getCustomerId();
        Authentication auth = checkAuth(customerId);
        List<CartProducts> cartProducts = cartProductsRepository.findAll().stream().filter(cart -> cart.getCart().getCartId() == cartId).toList();
        // List<CartProducts> cartProducts = orderRepository.getCartProductsByOrderId(orderId);
        List<GetOrderDetailsVm> orderDetails = new ArrayList<GetOrderDetailsVm>();
        for (CartProducts cartProducts2 : cartProducts) {
            OrderDetailsDto orderDto = new OrderDetailsDto();
            orderDto.setOrderId(orderId);
            orderDto.setCartId(cartProducts2.getCart().getCartId());
            orderDto.setCustomerId(cartProducts2.getCart().getCustomer().getCustomerId());
            orderDto.setProductId(cartProducts2.getProduct().getProductId());
            orderDto.setProductQuantity(cartProducts2.getProductQuantity());
            GetOrderDetailsVm orderDetailsVm = modelMapperManager.forResponse().map(orderDto, GetOrderDetailsVm.class);
            orderDetails.add(orderDetailsVm);
        }
        logger.info(auth.getName() + " isimli müşteri " + orderId + " numaralı sipariş detaylarını görüntüledi!");
        return orderDetails;
    }
}
