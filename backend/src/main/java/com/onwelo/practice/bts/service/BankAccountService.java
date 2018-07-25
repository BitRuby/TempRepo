package com.onwelo.practice.bts.service;

import com.onwelo.practice.bts.entity.BankAccount;
import com.onwelo.practice.bts.entity.Transfer;
import com.onwelo.practice.bts.exceptions.NotFoundException;
import com.onwelo.practice.bts.exceptions.MissingFieldException;
import com.onwelo.practice.bts.exceptions.NotValidField;
import com.onwelo.practice.bts.exceptions.UniqueFieldException;
import com.onwelo.practice.bts.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public List<BankAccount> getAllBankAccounts() {
        return new ArrayList<>(bankAccountRepository.findAll());
    }

    public List<Transfer> getTransfers(Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("could not found account with id=" + id));

        return new ArrayList<>(bankAccount.getTransfers());
    }

    public BankAccount getBankAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("could not found account with id=" + id));
    }

    public BankAccount getBankAccountByNumber(String accountNo) {
        return bankAccountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new NotFoundException("could not found account with accountNo=" + accountNo));
    }

    public BankAccount addBankAccount(BankAccount bankAccount) {
        if (bankAccount.getAccountNo() == null) {
            throw new MissingFieldException("missing bank account field= account no");
        } else if (!isValid(bankAccount.getAccountNo())) {
            throw new NotValidField(bankAccount.getAccountNo() + " IBAN is incorrect");
        }
        if (bankAccount.getFirstName() == null) {
            throw new MissingFieldException("missing bank account field= first name");
        }
        if (bankAccount.getLastName() == null) {
            throw new MissingFieldException("missing bank account field= last name");
        }

        BankAccount b = bankAccountRepository.findByAccountNo(bankAccount.getAccountNo()).orElse(null);
        if (b != null) {
            throw new UniqueFieldException("account no is already taken");
        }

        return bankAccountRepository.save(bankAccount);
    }

    public BankAccount updateBankAccount(BankAccount bankAccount) {
        if (bankAccount.getFirstName() == null) {
            throw new MissingFieldException("missing bank account field= first name");
        }
        if (bankAccount.getAccountNo() == null) {
            throw new MissingFieldException("missing bank account field= account no");
        }
        if (bankAccount.getLastName() == null) {
            throw new MissingFieldException("missing bank account field= last name");
        }

        BankAccount b = bankAccountRepository.findByAccountNo(bankAccount.getAccountNo()).orElse(null);
        if (b != null) {
            throw new UniqueFieldException("account no is already taken");
        }

        return bankAccountRepository.save(bankAccount);
    }

    public BankAccount deactivateBankAccount(Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("could not found account with id=" + id));

        bankAccount.setActive(false);
        return bankAccountRepository.save(bankAccount);
    }

    public boolean isValid(String accountNo) {
        if (accountNo == null) return false;

        //2521 otrzymujemy po rozkodowaniu PL - rozpatrujemy tylko polskie rachunki
        accountNo = accountNo.replace(" ", "");

        if (accountNo.length() != 26) return false;

        accountNo = accountNo.substring(2, 26) + "2521" + accountNo.substring(0, 2);

        BigInteger value = new BigInteger(accountNo);
        return value.mod(new BigInteger("97")).intValue() == 1;
    }
}