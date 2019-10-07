package com.soprasteria.hackaton.teagile.core.service.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "project")
public class ProjectEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id", updatable = false)
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "status", nullable = false)
	private String status;

	@OneToMany(mappedBy = "project")
	private List<TaskEntity> tasks;

	@OneToMany(mappedBy = "project")
	private List<MeetingEntity> meetings;
	
	@JoinTable(name = "user_has_project", joinColumns = @JoinColumn(name = "project_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false))
	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.PERSIST })
	private Set<UserEntity> users = new HashSet<>();
}
