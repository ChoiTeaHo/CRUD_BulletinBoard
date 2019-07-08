package member.service;

import java.util.Map;

public class JoinRequest {

	private String id;
	private String name;
	private String password;
	private String confirmPassword;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswrod() {
		return password;
	}

	public void setPasswrod(String passwrod) {
		this.password = passwrod;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	// 위 사항은 회원가입기능을 구현시 필요한 요청데이터를 보관하는 필드이다. (아이디,이름,암호,암호확인값)

	// 각 필드데이터가 유효한지검사.[파라미터 errors 맵객체는 에러정보를 담기위해 사용한다.]
	// 예를들어 id필드값이 올바르지 않으면 errors맵겍체에 id를 키값으로해서 True 를 추가한다.
	public void validate(Map<String, Boolean> errors) { // 좀있다 JoinHandler에 생성해서 전달할것이다.
		checkEmpty(errors, id, "id");
		checkEmpty(errors, name, "name");
		checkEmpty(errors, password, "password");
		checkEmpty(errors, confirmPassword, "confirmPassword");
		if (!isPasswordEqualToConfirm()) { // 암호와 확인값이 일치하지 않으면 nonMatch라는 에러키를 추가한다.
			errors.put("notMatch", Boolean.TRUE);
		}
	}

	// value가 값이 없는경우 errors 맵 객체의 fieldName 키에 True를 추가한다.
	private void checkEmpty(Map<String, Boolean> errors, String value, String fieldName) {
		if (value == null || value.isEmpty()) {
			errors.put(fieldName, Boolean.TRUE);
		}
	}

	// password필드와 confirmPassword필드값이 같은지 검사한다. (필드데이터가 유효한지 검사)
	public boolean isPasswordEqualToConfirm() {
		return password != null && password.equals(confirmPassword);
	}

}
