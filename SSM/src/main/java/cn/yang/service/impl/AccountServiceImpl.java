package cn.yang.service.impl;

import cn.yang.dao.IAccount;
import cn.yang.domain.Account;
import cn.yang.service.AccountService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    IAccount iAccount;

    @Override
    public List<Account> findAll() {
        System.out.println("业务层：查询所有账户");
        return iAccount.findAll();
    }

    @Override
    public void saveAccount(Account account) {
        System.out.println("业务层：保存账户");
        iAccount.saveAccount(account);
    }
}
