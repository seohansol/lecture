package com.kh.secom.member.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePasswordDTO {
	@Pattern(regexp="^[a-zA-Z0-9]*$")
	@Size(min=4, max=10)
	@NotBlank(message="현비입력!")
	private String currentPassword;
	
	@Pattern(regexp="^[a-zA-Z0-9]*$")
	@Size(min=4, max=10)
	@NotBlank(message="? 뭐해?")
	private String newPassword;

}
