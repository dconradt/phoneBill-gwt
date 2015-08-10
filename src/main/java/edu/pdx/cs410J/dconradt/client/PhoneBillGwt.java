package edu.pdx.cs410J.dconradt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.AbstractPhoneBill;

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
    private Label lblTitle;
    private Label lblCustomer;
    private Button btnAddCall;
    private Button btnDisplay;
    private MenuBar menu;
    private MenuBar actionMenu;
    private MenuBar helpMenu;
    private Grid callGrid;
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

        Command searchCalls = new Command() {
            public void execute() {
                searchCalls(rootPanel);
            }
        };

        actionMenu = new MenuBar(true);
        helpMenu = new MenuBar(true);
        actionMenu.addItem("Add Call", addCalls);
        actionMenu.addItem("Display All Calls", displayCalls);
        actionMenu.addItem("Search Calls", searchCalls);
        helpMenu.addItem("README", readMe);
        menu = new MenuBar();
        menu.addItem("Actions", actionMenu);
        menu.addItem("Help", helpMenu);
        rootPanel.add(menu);
  }

    private void searchCalls(RootPanel rootPanel) {
        rootPanel.clear();
        txtCustomerName = new TextBox();
        txtCustomerName.setMaxLength(20);
        txtStartTime = new TextBox();
        txtEndTime = new TextBox();
        lblCustomerName = new Label("Customer Name");
        lblCustomerName = new Label("Customer Name");
        lblStartTime = new Label("Start Time");
        lblEndTime = new Label("End Time");
        btnDisplay = new Button("Display Calls");
        rootPanel.add(txtCustomerName);
        rootPanel.add(txtStartTime);
        rootPanel.add(txtEndTime);
        rootPanel.add(lblCustomerName, lblFromLeft, lblFromTop);
        rootPanel.add(txtCustomerName, txtFromLeft, txtFromTop);
        rootPanel.add(lblStartTime, lblFromLeft, lblFromTop + 20);
        rootPanel.add(txtStartTime, txtFromLeft, txtFromTop + 20);
        rootPanel.add(lblEndTime, lblFromLeft, lblFromTop + 40);
        rootPanel.add(txtEndTime, txtFromLeft, txtFromTop + 40);
        rootPanel.add(btnDisplay, lblFromLeft, lblFromTop + 80);
        rootPanel.add(menu);
        btnDisplay.addClickHandler(displaySearchCalls(rootPanel));
    }

    private ClickHandler displaySearchCalls(final RootPanel rootPanel) {
        return new ClickHandler() {
            public void onClick( ClickEvent clickEvent )
            {

                rootPanel.clear();
                String customerName = txtCustomerName.getText();
                String callerNumber = "";
                String calleeNumber = "";
                final String startTime = txtStartTime.getText();
                final String endTime = txtEndTime.getText();

                PingServiceAsync async = GWT.create(PingService.class);
                async.ping(customerName, callerNumber, calleeNumber, startTime, endTime, new AsyncCallback<AbstractPhoneBill>() {
                    public void onFailure(Throwable ex) {
                        Window.alert(ex.toString());
                    }

                    public void onSuccess(AbstractPhoneBill phonebill) {
                        Collection<AbstractPhoneCall> calls = phonebill.getPhoneCalls();
                        String customerName = phonebill.getCustomer();
                        callGrid = new Grid();
                        int rows = phonebill.getPhoneCalls().size();
                        int cols = 5;
                        lblCustomer = new Label("Phone bill for customer: " + customerName);
                        callGrid = new Grid(rows + 1, cols);
                        callGrid.getColumnFormatter().setWidth(0, "10%");
                        callGrid.getColumnFormatter().setWidth(1, "10%");
                        callGrid.getColumnFormatter().setWidth(2, "10%");
                        callGrid.getColumnFormatter().setWidth(3, "10%");
                        callGrid.getColumnFormatter().setWidth(4, "10%");
                        callGrid.setText(0, 0, "Caller Number");
                        callGrid.setText(0, 1, "Callee Number");
                        callGrid.setText(0, 2, "Start Time");
                        callGrid.setText(0, 3, "End Time");
                        callGrid.setText(0, 4, "Duration");
                        int r = 1;
                        for (AbstractPhoneCall call : calls) {
                            Date newStart = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a").parse(startTime);
                            Date newEnd = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a").parse(endTime);
                            if (((PhoneCall) call).getStartTime().compareTo(newStart) >= 0 && ((PhoneCall) call).getStartTime().compareTo(newEnd) <= 0) {
                                Date endCall = null;
                                endCall = call.getEndTime();
                                Date startCall = null;
                                startCall = call.getStartTime();
                                long timeDifference = endCall.getTime() - startCall.getTime();
                                int duration = (int) (timeDifference / (60 * 1000));
                                callGrid.getColumnFormatter().setWidth(0, "10%");
                                callGrid.setText(r, 0, call.getCaller());
                                callGrid.setText(r, 1, call.getCallee());
                                callGrid.setText(r, 2, call.getStartTimeString());
                                callGrid.setText(r, 3, call.getEndTimeString());
                                callGrid.setText(r, 4, duration + " minutes");
                                ++r;
                            }
                        }
                        rootPanel.add(lblCustomer, lblFromLeft + 80, lblFromTop + 10);
                        rootPanel.add(callGrid, lblFromLeft + 80, lblFromTop + 40);
                        rootPanel.add(menu);


                    }
                });
            }
        };
    }


    private void listCalls(RootPanel rootPanel) {
        rootPanel.clear();
        txtCustomerName = new TextBox();
        txtCustomerName.setMaxLength(20);
        lblCustomerName = new Label("Customer Name");
        btnDisplay = new Button("Display Calls");
        rootPanel.add(lblCustomerName, lblFromLeft, lblFromTop);
        rootPanel.add(txtCustomerName, txtFromLeft, txtFromTop);
        rootPanel.add(btnDisplay, lblFromLeft, lblFromTop + 40);
        btnDisplay.addClickHandler(displayAllCalls(rootPanel));
        rootPanel.add(menu);

    }

    private void addCall(RootPanel rootPanel) {
        rootPanel.clear();
        txtCustomerName = new TextBox();
        txtCustomerName.setVisible(true);
        txtCustomerName.setMaxLength(20);
        txtCallerNumber = new TextBox();
        txtCallerNumber.setVisible(true);
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
        rootPanel.add(menu);
        btnAddCall.addClickHandler(createNewPhoneBillOnServer());
    }


    private ClickHandler displayAllCalls(final RootPanel rootPanel) {
        return new ClickHandler() {
            public void onClick( ClickEvent clickEvent )
            {
                rootPanel.clear();
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
                        callGrid = new Grid();
                        int rows = phonebill.getPhoneCalls().size();
                        int cols = 5;
                        lblCustomer = new Label("Phone bill for customer: " + customerName);
                        callGrid = new Grid(rows + 1, cols);
                        callGrid.getColumnFormatter().setWidth(0, "10%");
                        callGrid.getColumnFormatter().setWidth(1, "10%");
                        callGrid.getColumnFormatter().setWidth(2, "10%");
                        callGrid.getColumnFormatter().setWidth(3, "10%");
                        callGrid.getColumnFormatter().setWidth(4, "10%");
                        callGrid.setText(0,0, "Caller Number");
                        callGrid.setText(0, 1, "Callee Number");
                        callGrid.setText(0,2, "Start Time");
                        callGrid.setText(0, 3, "End Time");
                        callGrid.setText(0,4, "Duration");
                        int r = 1;
                        for ( AbstractPhoneCall call : calls ) {
                            Date endCall = null;
                            endCall = call.getEndTime();
                            Date startCall = null;
                            startCall = call.getStartTime();
                            long timeDifference = endCall.getTime() - startCall.getTime();
                            int duration = (int)(timeDifference / (60 * 1000));
                            callGrid.getColumnFormatter().setWidth(0, "10%");
                            callGrid.setText(r,0, call.getCaller());
                            callGrid.setText(r, 1, call.getCallee());
                            callGrid.setText(r, 2, call.getStartTimeString());
                            callGrid.setText(r, 3, call.getEndTimeString());
                            callGrid.setText(r, 4, duration + " minutes");
                            ++r;
                            }
                            rootPanel.add(lblCustomer, lblFromLeft + 80, lblFromTop + 10);
                            rootPanel.add(callGrid, lblFromLeft + 80, lblFromTop + 40);
                            rootPanel.add(menu);
                    }
                });
            }
        };
    }
    private ClickHandler createNewPhoneBillOnServer() {
        return new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                final String customerName = txtCustomerName.getText();
                if(customerName.isEmpty()){
                    Window.alert("The customer name cannot be empty.");
                    return;
                }
                final String callerNumber = txtCallerNumber.getText();
                if(!verifyNumber(callerNumber)) {
                    Window.alert("The caller number " + callerNumber + " must be of the form ddd-ddd-dddd");
                    return;
                }
                final String calleeNumber = txtCalleeNumber.getText();
                if(!verifyNumber(calleeNumber)) {
                    Window.alert("The callee number " + calleeNumber + " must be of the form ddd-ddd-dddd");
                    return;
                }
                final String startTime = txtStartTime.getText();
                if(!verifyTime(startTime)){
                    Window.alert("The start time " + startTime + " must be of the form mm/dd/yyy hh:mm am");
                    return;
                }
                final String endTime = txtEndTime.getText();
                if(!verifyTime(endTime)){
                    Window.alert("The end time " + endTime + " must be of the form mm/dd/yyy hh:mm am");
                    return;
                }
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
                        txtCustomerName.setText("");
                        txtCallerNumber.setText("");
                        txtCalleeNumber.setText("");
                        txtStartTime.setText("");
                        txtEndTime.setText("");
                    }
                });
            }
        };
    }

    private boolean verifyTime(String startEndTime) {
        if(startEndTime.matches("^(((((0[13578])|([13578])|(1[02]))[\\-\\/\\s]?((0[1-9])|([1-9])|([1-2][0-9])|(3[01])))|" +
                "((([469])|(11))[\\-\\/\\s]?((0[1-9])|([1-9])|([1-2][0-9])|(30)))|((02|2)[\\-\\/\\s]?((0[1-9])|([1-9])|" +
                "([1-2][0-9]))))[\\-\\/\\s]?\\d{4})(\\s(((0[1-9])|([1-9])|(1[0-2]))\\:([0-5][0-9])((\\s)|(\\:([0-5][0-9])\\s))([AM|PM|am|pm]{2,2})))?$"))

            return true;
        else
            return false;
    }

    private boolean verifyNumber(String theNumber) {
        if(theNumber.matches("\\d{3}-\\d{3}-\\d{4}"))
            return true;
        else
            return false;
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
