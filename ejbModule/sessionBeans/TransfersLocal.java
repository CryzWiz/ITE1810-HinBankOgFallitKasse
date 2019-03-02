package sessionBeans;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import entity.Transfer;

@Local
public interface TransfersLocal {

	public void add(Transfer transfer) throws Exception;
	
	public List<Transfer> getAllTransfers(String accountNumber) throws Exception;
	
	public List<Transfer> getAllTransfersInTimeframe(Date fromDate, Date toDate, String accountNumber) throws Exception;
}
