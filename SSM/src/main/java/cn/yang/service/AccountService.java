package cn.yang.service;

import cn.yang.domain.Account;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface AccountService {
    List<Account> findAll();

    void saveAccount(Account account);
}
