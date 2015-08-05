package edu.pdx.cs410J.dconradt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.AbstractPhoneBill;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * A basic GWT class that makes sure that we can send an Phone Bill back from the server
 */
public class PhoneBillGwt implements EntryPoint {

    private TextBox txtCustomerName;
    private TextBox txtCallerNumber;
    private TextBox txtCalleeNumber;
    private TextBox txtStartTime;
    private TextBox txtEndTime;
    private Label lblCustomerName;
    private Label lblCallerNumber;
    private Label lblCalleeNumber;
    private Label lblStartTime;
    private Label lblEndTime;
    private Label lblHeader;
    private Label lblPhoneCall;
    private Button btnAddCall;
    private Button btnDisplay;
    private MenuBar menu;
    private MenuBar actionMenu;
    private MenuBar helpMenu;
    private int txtFromLeft = 125;
    private int txtFromTop = 100;
    private int lblFromLeft = 20;
    private int lblFromTop = 100;

    public void onModuleLoad() {
        Command readMe = new Command() {
            public void execute() {
                displayReadMe();
            }
        };

        final RootPanel rootPanel = RootPanel.get();

        Command addCalls = new Command() {
            public void execute() {
                addCall(rootPanel);
            }
        };

        Command displayCalls = new Command() {
            public void execute() {
                listCalls(rootPanel);
            }
        };

        actionMenu = new MenuBar(true);
        helpMenu = new MenuBar(true);
        actionMenu.addItem("Add Call",addCalls);
        actionMenu.addItem("Display All Calls", displayCalls);
        actionMenu.addItem("Search Calls", readMe);
        helpMenu.addItem("README", readMe);
        menu = new MenuBar();
        menu.addItem("Actions", actionMenu);
        menu.addItem("Help", helpMenu);
        rootPanel.add(menu);


  }

    private void clearWindow() {
        txtCustomerName.setText(null);
        txtCustomerName.setVisible(false);
        lblCustomerName.setVisible(false);
        btnDisplay.setVisible(false);
        lblHeader.setVisible(false);
        txtCustomerName.setText(null);
        txtCustomerName.setVisible(false);
        txtCallerNumber.setText(null);
        txtCallerNumber.setVisible(false);
        txtCalleeNumber.setText(null);
        txtCalleeNumber.setVisible(false);
        txtStartTime.setText(null);
        txtStartTime.setVisible(false);
        txtEndTime.setText(null);
        txtEndTime.setVisible(false);
        btnAddCall.setVisible(false);
        lblCustomerName.setVisible(false);
        lblCallerNumber.setVisible(false);
        lblCalleeNumber.setVisible(false);
        lblStartTime.setVisible(false);
        lblEndTime.setVisible(false);
    }

    private void listCalls(RootPanel rootPanel) {
        txtCustomerName = new TextBox();
        txtCustomerName.setMaxLength(20);
        lblCustomerName = new Label("Customer Name");
        btnDisplay = new Button("Display Calls");
        btnDisplay.addClickHandler(displayAllCalls(rootPanel));
        rootPanel.add(lblCustomerName, lblFromLeft, lblFromTop);
        rootPanel.add(txtCustomerName, txtFromLeft, txtFromTop);
        rootPanel.add(btnDisplay, lblFromLeft, lblFromTop + 40);

    }

    private void addCall(RootPanel rootPanel) {

        txtCustomerName = new TextBox();
        txtCustomerName.setMaxLength(20);
        txtCallerNumber = new TextBox();
        txtCalleeNumber = new TextBox();
        txtStartTime = new TextBox();
        txtEndTime = new TextBox();
        lblCustomerName = new Label("Customer Name");
        lblCallerNumber = new Label("Caller Number");
        lblCalleeNumber = new Label("Callee Number");
        lblStartTime = new Label("Start Time");
        lblEndTime = new Label("End Time");
        btnAddCall = new Button("Submit");
        rootPanel.add(txtCustomerName);
        rootPanel.add(txtCallerNumber);
        rootPanel.add(txtCalleeNumber);
        rootPanel.add(txtStartTime);
        rootPanel.add(txtEndTime);
        rootPanel.add(lblCustomerName, lblFromLeft, lblFromTop);
        rootPanel.add(txtCustomerName, txtFromLeft, txtFromTop);
        rootPanel.add(lblCallerNumber, lblFromLeft, lblFromTop + 20);
        rootPanel.add(txtCallerNumber, txtFromLeft, txtFromTop + 20);
        rootPanel.add(lblCalleeNumber, lblFromLeft, lblFromTop + 40);
        rootPanel.add(txtCalleeNumber, txtFromLeft, txtFromTop + 40);
        rootPanel.add(lblStartTime, lblFromLeft, lblFromTop + 60);
        rootPanel.add(txtStartTime, txtFromLeft, txtFromTop + 60);
        rootPanel.add(lblEndTime, lblFromLeft, lblFromTop + 80);
        rootPanel.add(txtEndTime, txtFromLeft, txtFromTop + 80);
        rootPanel.add(btnAddCall, lblFromLeft, lblFromTop + 120);
        btnAddCall.addClickHandler(createNewPhoneBillOnServer());
    }


    private ClickHandler displayAllCalls(final RootPanel rootPanel) {
        return new ClickHandler() {
            public void onClick( ClickEvent clickEvent )
            {

                String customerName = txtCustomerName.getText();
                String callerNumber = "";
                String calleeNumber = "";
                String startTime = "";
                String endTime = "";

                PingServiceAsync async = GWT.create(PingService.class);
                async.ping(customerName,callerNumber, calleeNumber,startTime, endTime, new AsyncCallback<AbstractPhoneBill>() {
                    public void onFailure( Throwable ex )
                    {
                        Window.alert(ex.toString());
                    }

                    public void onSuccess( AbstractPhoneBill phonebill )
                    {
                        Collection<AbstractPhoneCall> calls = phonebill.getPhoneCalls();

                        String customerName = phonebill.getCustomer();
                        lblHeader = new Label("Customer Phone Bill Customer Name: " + customerName + "\n\n\tCaller " +
                                "Number\tCallee Number\tStarting Call Time\t\tEnding Call Time\t\tDuration of call\n");
                        rootPanel.add(lblHeader);
                        for ( AbstractPhoneCall call : calls ) {
                           // SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                           // Date endCall = null;
                            //try {
                           //     endCall = dateFormatter.parse(call.getEndTimeString());
                           // } catch (ParseException e) {
                           //     System.out.println("Error calculating phone call duration.");
                           // }
                           // Date startCall = null;
                           //try {
                            //    startCall = dateFormatter.parse(call.getStartTimeString());
                           // } catch (ParseException e) {
                            //    System.out.println("Error calculating phone call duration.");
                           // }
                            //long timeDifference = endCall.getTime() - startCall.getTime();
                            //int duration = (int)(timeDifference / (60 * 1000));
                            String phoneBillRecord = "\t" + call.getCaller() + "\t" + call.getCallee() + "\t"
                                    + call.getStartTimeString() + "\t\t" + call.getEndTimeString() + "\t\t";

                            lblPhoneCall = new Label(phoneBillRecord );
                            rootPanel.add(lblPhoneCall);
                        }
                        txtCustomerName.setText(null);
                        txtCustomerName.setVisible(false);
                        lblCustomerName.setVisible(false);
                        btnDisplay.setVisible(false);
                       // lblHeader.setVisible(false);
                    }
                });
            }
        };
    }
    private ClickHandler createNewPhoneBillOnServer() {
        return new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {

                final String customerName = txtCustomerName.getText();
                final String callerNumber = txtCallerNumber.getText();
                final String calleeNumber = txtCalleeNumber.getText();
                final String startTime = txtStartTime.getText();
                final String endTime = txtEndTime.getText();
                PingServiceAsync async = GWT.create(PingService.class);
                async.ping(customerName, callerNumber, calleeNumber, startTime, endTime, new AsyncCallback<AbstractPhoneBill>() {
                    public void onFailure(Throwable ex) {
                        Window.alert(ex.toString());
                    }

                    public void onSuccess(AbstractPhoneBill phonebill) {
                        int length;
                        Collection<AbstractPhoneCall> calls = phonebill.getPhoneCalls();
                        length = calls.size();
                        Window.alert(phonebill.getCustomer() + "'s phone call from " +
                                callerNumber + " to " + calleeNumber + " between " +
                                startTime + " and " + endTime + " was added" +
                                " to the phone bill.");

                        txtCustomerName.setText(null);
                        txtCustomerName.setVisible(false);
                        txtCallerNumber.setText(null);
                        txtCallerNumber.setVisible(false);
                        txtCalleeNumber.setText(null);
                        txtCalleeNumber.setVisible(false);
                        txtStartTime.setText(null);
                        txtStartTime.setVisible(false);
                        txtEndTime.setText(null);
                        txtEndTime.setVisible(false);
                        btnAddCall.setVisible(false);
                        lblCustomerName.setVisible(false);
                        lblCallerNumber.setVisible(false);
                        lblCalleeNumber.setVisible(false);
                        lblStartTime.setVisible(false);
                        lblEndTime.setVisible(false);
                    }
                });
            }

        };
    }

    /**
     * Pop up the readme text.
     */
    public void displayReadMe() {
        Window.alert("This program uses google web tools to create a phone bill web application.  The application will" +
                " provide a menu of actions to perform on a phonebill, add and search, and a help menu item which will " +
                "display this message in a popup window.  The use of the PhoneBill and PhoneCall classes will hold phone bill " +
                "informationn during the web session.  Pretty Print will be utilized to display phone bill infomation.");
    }
}
