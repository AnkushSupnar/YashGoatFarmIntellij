package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.BankTransactionDao;
import main.java.main.java.hibernate.dao.daoImpl.BankTransactionDaoImpl;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.service.service.BankTransactionService;

import java.time.LocalDate;
import java.util.List;

public class BankTransactionServiceImpl implements BankTransactionService {

	private BankTransactionDao dao;
	public BankTransactionServiceImpl() {
		this.dao = new BankTransactionDaoImpl();
	}
	@Override
	public BankTransaction getBankTransactionById(long id) {
		return dao.getBankTransactionById(id);
	}

	@Override
	public List<BankTransaction> getAllBankTransaction() {
		return dao.getAllBankTransaction();
	}

	@Override
	public List<BankTransaction> getBankTransaction(int bankid) {
		return dao.getBankTransaction(bankid);
	}

	@Override
	public List<BankTransaction> getAllDebitTransaction() {
		return dao.getAllDebitTransaction();
	}

	@Override
	public List<BankTransaction> getAllCreditTransaction() {
		return dao.getAllCreditTransaction();
	}

	@Override
	public List<BankTransaction> getAllDebitTransaction(int bankid) {
		return dao.getAllDebitTransaction(bankid);
	}

	@Override
	public List<BankTransaction> getAllCreditTransaction(int bankid) {
		return dao.getAllDebitTransaction(bankid);
	}

	@Override
	public int saveBankTransaction(BankTransaction btr) {
		return dao.saveBankTransaction(btr);
	}
	@Override
	public void deleteBankTransaction(String particulars) {
		BankTransaction tr= dao.getBankTransactionByPartucular(particulars);
		dao.deleteBankTransaction(tr.getId());
	}
	@Override
	public List<BankTransaction> getPeriodWiseBankTransaction(LocalDate fromDate, LocalDate toDate, int bankid) {
		return dao.getPeriodWiseBankTransaction(fromDate, toDate, bankid);
	}
	@Override
	public BankTransaction getBankTransactionByPartucular(String particular) {
		
		return dao.getBankTransactionByPartucular(particular);
	}
	@Override
	public void deleteBankTransaction(long id) {
		dao.deleteBankTransaction(id);
		
	}
	@Override
	public float getOpenigBalance(int bankid, LocalDate date) {
		try {
			System.out.println("Get Date upto"+date.minusDays(1));
			BankTransaction tr = getTransactionByParticular("Opening Balance", bankid);
			BankTransaction firsttr = getBankTransactionById(1);
			float opening = tr.getDebit();
			float debit=0,credit=0;
			List<BankTransaction>list = getPeriodWiseBankTransaction(firsttr.getDate(), date.minusDays(1), bankid);
			for(BankTransaction t:list)
			{
				System.out.println(t.getDate());
				if(t.getId()!=tr.getId())
				{
					debit = debit+t.getDebit();
					credit = credit+t.getCredit();
				}
			}
			return opening-debit+credit;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	@Override
	public BankTransaction getTransactionByParticular(String particular, int bankid) {
		return dao.getTransactionByParticular(particular, bankid);
	}

}
