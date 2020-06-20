package com.cczu.librarymanagementserver.controller;


import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Category;
import com.cczu.librarymanagementserver.service.impl.CategoryServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("category")
@Api(tags = "类别")
public class CategoryController {
	@Autowired
	private CategoryServiceImpl categoryService;

	/**
	 * 自定义XML通过pid查询分类列表 pid 为-1
	 *
	 * @return
	 */
	@GetMapping
	public RespBean getCatListsByPid() {
		return categoryService.getCatListsByPid(-1);
	}

	/**
	 * 插入新分类
	 *
	 * @param category
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean addCat(@RequestBody Category category) {
		System.out.println(category);
		return categoryService.addRecord(category);

	}

	/**
	 * 更新分类
	 *
	 * @param category
	 * @return
	 */
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean updateCat(@RequestBody @Validated  Category category) {
		return categoryService.updateRecord(category);

	}

	/**
	 * 删除分类
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean deleteCat(Integer id) {
		return categoryService.deleteRecordById(id);
	}

	/**
	 * 获取所有叶子节点
	 *
	 * @return
	 */
	@GetMapping("leaf")
	public RespBean getLeafCats() {
		return categoryService.getLeafCats();
	}

	/**
	 * 添加根节点
	 *
	 * @param category
	 * @return
	 */
	@PostMapping("root")
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean addRootCat(@RequestBody Category category) {
		return categoryService.addRootCat(category);
	}
}
