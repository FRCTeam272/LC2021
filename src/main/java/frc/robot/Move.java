package frc.robot;

public class Move {
	private String code;
	private String description;
	private String value1;
	private String value2;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStringValue1() {
		return value1;
	}
	
	public Double getValue1() {
		try {
			return Double.valueOf(this.getStringValue1());
		} catch (Exception e) {
			return 0.0;
		}
	}

	public void setValue1(String value1) {
		this.value1 = value1.trim();
	}

	public Double getValue2() {
		try {
			return Double.valueOf(this.getStringValue2());
		} catch (Exception e) {
			return 0.0;
		}
		
	}

	public String getStringValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2.trim();
	}
	

}
