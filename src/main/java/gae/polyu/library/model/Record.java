package gae.polyu.library.model;

public class Record {
    private String title;
    private String link;
    private String barcode;
    private String duedate;
    private String callnumber;
    
    public Record() {
    }
    
    public Record(String title, String link, String barcode, String duedate, String callnumber) {
        this.title = title;
        this.link = link;
        this.barcode = barcode;
        this.duedate = duedate;
        this.callnumber = callnumber;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getLink() {
        return link;
    }
    
    public String getBarcode() {
        return barcode;
    }
    
    public String getDuedate() {
        return duedate;
    }
    
    public String getCallnumber() {
        return callnumber;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    
    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }
    
    public void setCallnumber(String callnumber) {
        this.callnumber = callnumber;
    }
}