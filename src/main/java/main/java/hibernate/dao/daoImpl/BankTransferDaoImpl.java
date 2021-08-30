package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.BankTransferDao;
import main.java.main.java.hibernate.entities.BankTransfer;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class BankTransferDaoImpl implements BankTransferDao {
    public BankTransferDaoImpl() {
    }

    public BankTransfer getBankTransferById(int id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            BankTransfer var3;
            try {
                session.beginTransaction();
                var3 = (BankTransfer)session.get(BankTransfer.class, id);
            } catch (Throwable var6) {
                if (session != null) {
                    try {
                        session.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (session != null) {
                session.close();
            }

            return var3;
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public List<BankTransfer> getAllBankTransfer() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            List var3;
            try {
                session.beginTransaction();
                String hql = "from BankTransfer";
                var3 = session.createQuery(hql, BankTransfer.class).list();
            } catch (Throwable var5) {
                if (session != null) {
                    try {
                        session.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }

                throw var5;
            }

            if (session != null) {
                session.close();
            }

            return var3;
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public List<BankTransfer> getBankTransferByDate(LocalDate date) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            List var4;
            try {
                session.beginTransaction();
                String hql = "from BankTransfer where date=:date";
                var4 = session.createQuery(hql, BankTransfer.class).setParameter("date", date).list();
            } catch (Throwable var6) {
                if (session != null) {
                    try {
                        session.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (session != null) {
                session.close();
            }

            return var4;
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public List<BankTransfer> getBankTransferByDatePeriod(LocalDate from, LocalDate to) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            List var5;
            try {
                session.beginTransaction();
                String hql = "from BankTransfer where date between :fromDate and :toDate";
                var5 = session.createQuery(hql, BankTransfer.class).setParameter("fromDate", from).setParameter("toDate", to).list();
            } catch (Throwable var7) {
                if (session != null) {
                    try {
                        session.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }

                throw var7;
            }

            if (session != null) {
                session.close();
            }

            return var5;
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public List<BankTransfer> getBankTransferByBank(int bankid) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            List var4;
            try {
                session.beginTransaction();
                String hql = "from BankTransaction where frombankid:bankid and tobankid=:tobankid";
                var4 = session.createQuery(hql, BankTransfer.class).setParameter("bankid", bankid).setParameter("tobankid", bankid).list();
            } catch (Throwable var6) {
                if (session != null) {
                    try {
                        session.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (session != null) {
                session.close();
            }

            return var4;
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public int saveBankTransfer(BankTransfer transfer) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            byte var3;
            label48: {
                try {
                    session.beginTransaction();
                    if (transfer.getId() == 0) {
                        session.save(transfer);
                        session.getTransaction().commit();
                        var3 = 1;
                        break label48;
                    }

                    session.update(transfer);
                    session.getTransaction().commit();
                    var3 = 2;
                } catch (Throwable var6) {
                    if (session != null) {
                        try {
                            session.close();
                        } catch (Throwable var5) {
                            var6.addSuppressed(var5);
                        }
                    }

                    throw var6;
                }

                if (session != null) {
                    session.close();
                }

                return var3;
            }

            if (session != null) {
                session.close();
            }

            return var3;
        } catch (Exception var7) {
            var7.printStackTrace();
            return 0;
        }
    }
}
