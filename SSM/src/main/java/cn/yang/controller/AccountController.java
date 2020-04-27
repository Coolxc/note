package cn.yang.controller;

import cn.yang.domain.Account;
import cn.yang.service.AccountService;
import cn.yang.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//web层（MVC）
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @RequestMapping("/findall")
    public String findall(Model model){
        System.out.println("表现层：返回所有账户信息");
        //调用service业务层
        List<Account> accounts = accountService.findAll();
        model.addAttribute("accounts",accounts);
        return "list";
    }

    @RequestMapping("/saveaccount")
    public String save(Account account,HttpServletResponse response, HttpServletRequest request) throws IOException {
        accountService.saveAccount(account);
        //重定向到显示账户页面
        response.sendRedirect(request.getContextPath() + "/account/findall");
        return "success";
    }
}
