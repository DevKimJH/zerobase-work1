package dto;

import java.util.Date;

public class SearchHistory {
    private int HISTORY_MGR_NO; // 히스토리 관리 번호(key)
    private double LAT;	// 위도
    private double LNT;	// 경도
    private Date registerDate; // 조회일자

    public int getHISTORY_MGR_NO() {
        return HISTORY_MGR_NO;
    }
    public void setHISTORY_MGR_NO(int hISTORY_MGR_NO) {
        HISTORY_MGR_NO = hISTORY_MGR_NO;
    }
    public double getLAT() {
        return LAT;
    }
    public void setLAT(double lAT) {
        LAT = lAT;
    }
    public double getLNT() {
        return LNT;
    }
    public void setLNT(double lNT) {
        LNT = lNT;
    }
    public Date getRegisterDate() {
        return registerDate;
    }
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

}
