package com.btk.ordercorner.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btk.ordercorner.model.dto.CategoryDto;
import com.btk.ordercorner.model.vm.AddCategoryVm;
import com.btk.ordercorner.model.vm.UpdateCategoryVm;
import com.btk.ordercorner.service.CategoryService;
import com.btk.ordercorner.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/categories/")
@Tag(name = "Kategori İşlemleri")
@SecurityRequirement(name = "deneme")
public class CategoryController {

    private CategoryService categoryService;
    private ReportService reportService;

    public CategoryController(CategoryService categoryService, ReportService reportService) {
        this.categoryService = categoryService;
        this.reportService = reportService;
    }

    @GetMapping(value="/report/{format}")
    public String generateReport(@PathVariable("format") String format) throws FileNotFoundException, JRException {
        return reportService.exportReportCategory(format);
    }
    

    @GetMapping(value="all")
    @Operation(
        description = "Bu metod, sistemde bulunan tüm kategorileri listelemek için kullanılır. Kategoriler, içerik veya öğeleri gruplandırmak amacıyla kullanılan önemli bir organizasyon aracıdır. Bu metod sayesinde kullanıcılar, mevcut olan bütün kategori seçeneklerini görüntüleyebilirler",
        summary =   "Bu metod tüm kategorileri listeler.",
        responses = {
            @ApiResponse(
                description = "Başarılı",
                responseCode = "200"
            ),
            @ApiResponse(
                description = "Geçersiz istek",
                responseCode = "400"
            ),
            @ApiResponse(
                description = "Yetkisiz işlem",
                responseCode = "403"
            ),
            @ApiResponse(
                description = "Bulunamadı",
                responseCode = "404"
            )
        }
    )
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }


    @GetMapping(value="/{categoryId}")
    @Operation(
        description = "Bu metod, belirli bir kategoriye ait verileri listelemek için kullanılır. {id} parametresi, istenen kategorinin benzersiz tanımlayıcısını belirtir. API'ye gönderilen bu {id} parametresi sayesinde, ilgili kategoriye ait olan Kategoriler filtrelenir ve listelenir.",
        summary =   "Bu metod, {id} parametresi gönderilen belirtilen kategoriyi listelemek için kullanılır.",
        responses = {
            @ApiResponse(
                description = "Başarılı",
                responseCode = "200"
            ),
            @ApiResponse(
                description = "Geçersiz istek",
                responseCode = "400"
            ),
            @ApiResponse(
                description = "Yetkisiz işlem",
                responseCode = "403"
            ),
            @ApiResponse(
                description = "Bulunamadı",
                responseCode = "404"
            )
        }
    )
    public CategoryDto getByCategoryId(@PathVariable("categoryId") int categoryId) {
        return categoryService.getByCategoryId(categoryId);
    }
    

    @PostMapping(value="add")
    @Operation(
        description = "Bu metod, yalnızca \"Admin\" rolüne sahip kullanıcıların yeni kategoriler eklemesi için kullanılır. Yeni bir kategori eklemek isteyen \"Admin\", bu metod aracılığıyla gerekli bilgileri sisteme iletebilir.",
        summary =   "Bu metod, sadece \"Admin\" rolündeki kullanıcıların yeni kategoriler eklemesine izin verir. ",
        responses = {
            @ApiResponse(
                description = "Başarılı",
                responseCode = "200"
            ),
            @ApiResponse(
                description = "Geçersiz istek",
                responseCode = "400"
            ),
            @ApiResponse(
                description = "Yetkisiz işlem",
                responseCode = "403"
            ),
            @ApiResponse(
                description = "Bulunamadı",
                responseCode = "404"
            )
        }
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public int addNewCategory(@Valid @RequestBody AddCategoryVm categoryVm) {
        return categoryService.addNewCategory(categoryVm);
    }
    
    @DeleteMapping("/{categoryId}")
    @Operation(
        description = "Bu metod, yalnızca \"Admin\" yetkisine sahip olan kullanıcıların belirtilen \"id\" kategorisine göre silme işlemi gerçekleştirmesi için kullanılır.",
        summary =   "Bu metod, sadece \"Admin\" rolündeki kullanıcıların belirtilen kategoriyi silmesine izin verir. ",
        responses = {
            @ApiResponse(
                description = "Başarılı",
                responseCode = "200"
            ),
            @ApiResponse(
                description = "Geçersiz istek",
                responseCode = "400"
            ),
            @ApiResponse(
                description = "Yetkisiz işlem",
                responseCode = "403"
            ),
            @ApiResponse(
                description = "Bulunamadı",
                responseCode = "404"
            )
        }
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteByCategoryId(@PathVariable("categoryId") int categoryId) {
        return categoryService.deleteByCategoryId(categoryId); 
    }

    @PutMapping(value="/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateCategory(@Valid @RequestBody UpdateCategoryVm categoryVm) {
        return categoryService.updateCategory(categoryVm);
    }
}
