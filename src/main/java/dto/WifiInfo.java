package dto;

import java.util.Date;

public class WifiInfo {
    private double dist;		// 거리
    private String mgrNo;		// 관리번호
    private String wrdofc;		// 자치구
    private String mainNm;		// 와이파이명
    private String adres1;		// 도로명주소
    private String adres2;		// 나머지주소
    private String instlFloor;	// 설치위치
    private String instlTy;		// 설치유형
    private String instlMby;	// 설치기관
    private String svcSe;		// 서비스구분
    private String cmcwr;		// 망종류
    private String cnstcYear;	// 설치년도
    private String inoutDoor;	// 실내외구분
    private String remars3;		// wifi접속환경
    private double lat;			// x좌표
    private double lnt;			// y좌표
    private Date workDttm;	// 작업일자


    public double getDist() {
        return dist;
    }
    public void setDist(double dist) {
        this.dist = dist;
    }
    public String getMgrNo() {
        return mgrNo;
    }
    public void setMgrNo(String mgrNo) {
        this.mgrNo = mgrNo;
    }
    public String getWrdofc() {
        return wrdofc;
    }
    public void setWrdofc(String wrdofc) {
        this.wrdofc = wrdofc;
    }
    public String getMainNm() {
        return mainNm;
    }
    public void setMainNm(String mainNm) {
        this.mainNm = mainNm;
    }
    public String getAdres1() {
        return adres1;
    }
    public void setAdres1(String adres1) {
        this.adres1 = adres1;
    }
    public String getAdres2() {
        return adres2;
    }
    public void setAdres2(String adres2) {
        this.adres2 = adres2;
    }
    public String getInstlFloor() {
        return instlFloor;
    }
    public void setInstlFloor(String instlFloor) {
        this.instlFloor = instlFloor;
    }
    public String getInstlTy() {
        return instlTy;
    }
    public void setInstlTy(String instlTy) {
        this.instlTy = instlTy;
    }
    public String getInstlMby() {
        return instlMby;
    }
    public void setInstlMby(String instlMby) {
        this.instlMby = instlMby;
    }
    public String getSvcSe() {
        return svcSe;
    }
    public void setSvcSe(String svcSe) {
        this.svcSe = svcSe;
    }
    public String getCmcwr() {
        return cmcwr;
    }
    public void setCmcwr(String cmcwr) {
        this.cmcwr = cmcwr;
    }
    public String getCnstcYear() {
        return cnstcYear;
    }
    public void setCnstcYear(String cnstcYear) {
        this.cnstcYear = cnstcYear;
    }
    public String getInoutDoor() {
        return inoutDoor;
    }
    public void setInoutDoor(String inoutDoor) {
        this.inoutDoor = inoutDoor;
    }
    public String getRemars3() {
        return remars3;
    }
    public void setRemars3(String remars3) {
        this.remars3 = remars3;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLnt() {
        return lnt;
    }
    public void setLnt(double lnt) {
        this.lnt = lnt;
    }
    public Date getWorkDttm() {
        return workDttm;
    }
    public void setWorkDttm(Date workDttm) {
        this.workDttm = workDttm;
    }



}
