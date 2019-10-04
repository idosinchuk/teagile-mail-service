package com.soprasteria.hackaton.teagile.core.service.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@NoArgsConstructor
@Data
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", updatable = false)
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "surname", nullable = false)
	private String surname;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@ManyToMany(mappedBy = "users")
    private Set<ProjectEntity> projects = new HashSet<>();

}
