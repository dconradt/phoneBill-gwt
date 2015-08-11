package edu.pdx.cs410J.dconradt.client;

import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.AbstractPhoneBill;

import java.lang.Override;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Dan Conradt 8/11/2015
 * Class to manage the getters and setters for phone bill requests.
 */
public class PhoneBill extends AbstractPhoneBill
{
  private String customerName;

  private Collection<AbstractPhoneCall> calls = new ArrayList<AbstractPhoneCall>();

  public PhoneBill(String customerName) {
    this.customerName = customerName;
  }

  public PhoneBill() {
  }

  @Override
  public String getCustomer() {
    return this.customerName;
  }

  @Override
  public void addPhoneCall(AbstractPhoneCall call) {

    this.calls.add(call);
  }

  @Override
  public Collection getPhoneCalls() {
    Collection newList = calls;
    Collections.sort((List<Comparable>) newList);
    return newList;
  }

}
