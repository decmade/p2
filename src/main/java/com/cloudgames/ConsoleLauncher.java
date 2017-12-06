package com.cloudgames;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cloudgames.dbal.HibernateSessionUtil;
import com.cloudgames.entities.User;
import com.cloudgames.entities.interfaces.UserInterface;

public class ConsoleLauncher {
	public static void main(String[] args) {
		Session session = HibernateSessionUtil.getSession();
//		Transaction tx = session.beginTransaction();
//		
//		User user = new User();
//		user.setLastName("Brown");
//		user.setFirstName("John");
//		
//		session.persist(user);
//		
//		tx.commit();
		UserInterface user = (UserInterface)session.get(User.class, 1);
		System.out.println(user.getLastName() );
		
		session.close();
		
		class A{
			  public A() {} // A1
			  public A(String s) {  this();  System.out.println("A :"+s);  }  // A2
			}

			class B extends A{
			  public int B(String s) {  System.out.println("B :"+s);  return 0; } // B1
			}
			class C extends B{
			    private C(){ super(); } // C1
			    public C(String s){  this();  System.out.println("C :"+s);  } // C2
			    public C(int i){} // C3
			}
	}
}
