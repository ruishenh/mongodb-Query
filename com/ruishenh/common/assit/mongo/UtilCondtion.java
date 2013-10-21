package com.ruishenh.common.assit.mongo;

/**
 * 条件查询辅助类mongodb专用
 * @author houchangren
 *
 */
public class UtilCondtion {
	
	public UtilCondtion(){
		
	}
	
	public	static String roundRepeatName(){
		 return IUtilCondtion.ENUM_REPEAT+Math.floor(Math.random()*100);
	}
	public UtilCondtion(String regexValue){
		setRegexValue(regexValue);
	}
	
	Integer dirRegex;
	
	String regexValue;
	
	Integer dirOrderBy;
	
	Integer dirCompare;
	
	Object compareValue;
	
	/**
	 * 
	 * @param regexValue specified regex
	 * @param dirRegex ---null-不模糊,0-左模糊,1-全模糊,2-右模糊
	 * @param dirOrderBy---null-默认排序,0-正方向,1-负方向
	 */
	public UtilCondtion(String regexValue,Integer dirRegex,Integer dirOrderBy){
		setRegexValue(regexValue);
		setDirRegex(dirRegex);
		setDirOrderBy(dirOrderBy);
	}
	/**
	 * 
	 * @param regexValue specified regex
	 * @param dirRegex ---null-不模糊,0-左模糊,1-全模糊,2-右模糊
	 */
	public UtilCondtion(String regexValue,Integer dirRegex){
		setRegexValue(regexValue);
		setDirRegex(dirRegex);
	}
	/**
	 * 只能构造排序和比对的函数
	 * @param value
	 * @param mark
	 * 比较方向eq=0,lt=1,gt=2,gte=3,lte=4
	 */
	public UtilCondtion(Object oValue,IUtilCondtion.Mark mark,Integer value){
		if (value==null||null==oValue) {
			return;
		}
		if (IUtilCondtion.Mark.COMPARE==mark) {
			setDirCompare(value);
			setCompareValue(oValue);
		}
		if (IUtilCondtion.Mark.ORDERBY==mark) {
			setDirOrderBy(value);
		}
	}
	/**
	 * 
	 * @param dirOrderBy ---null-默认排序,0-正方向,1-负方向
	 */
	public UtilCondtion(Integer dirOrderBy){
		setDirOrderBy(dirOrderBy);
	}

	/**模糊查询的标识：null-不模糊,0-左模糊,1-全模糊,2-右模糊*/
	public Integer getDirRegex() {
		return dirRegex;
	}

	public void setDirRegex(Integer dirRegex) {
		this.dirRegex = dirRegex;
	}
	/**排序方向：null-默认排序,0-正方向,1-负方向*/
	public Integer getDirOrderBy() {
		return dirOrderBy;
	}
	public void setDirOrderBy(Integer dirOrderBy) {
		this.dirOrderBy = dirOrderBy;
	}
	/**模糊查询字段值*/
	public String getRegexValue() {
		return regexValue;
	}
	public void setRegexValue(String regexValue) {
		this.regexValue = regexValue;
	}
	/**比较方向eq=0,lt=1,gt=2,gte=3,lte=4*/
	public Integer getDirCompare() {
		return dirCompare;
	}
	public void setDirCompare(Integer dirCompare) {
		this.dirCompare = dirCompare;
	}
	public Object getCompareValue() {
		return compareValue;
	}
	public void setCompareValue(Object compareValue) {
		this.compareValue = compareValue;
	}
}
