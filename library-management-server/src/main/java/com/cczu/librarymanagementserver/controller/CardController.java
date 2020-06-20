package com.cczu.librarymanagementserver.controller;


import com.aliyuncs.utils.StringUtils;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("card")
@CrossOrigin(origins="*")
public class CardController {

	@Autowired
	private CardService cardService;

	@GetMapping
	public RespBean read(){
		String cardId = cardService.read();
		if(StringUtils.isEmpty(cardId))
			return RespBean.error("查询智能卡失败","");
		return RespBean.ok("查询智能卡成功",cardId);
	}
	@GetMapping("ntg")
	public RespBean readNtg(){
		String cardId = cardService.readNtg();
		if(StringUtils.isEmpty(cardId))
			return RespBean.error("查询智能卡失败","");
		return RespBean.ok("查询智能卡成功",cardId);
	}

	@PostMapping
	public RespBean write(@RequestBody String cardId){
		Boolean flag = cardService.write(cardId);
		if (flag) return RespBean.ok("写入成功",true);
		else return RespBean.ok("写入失败",false);
	}
}
