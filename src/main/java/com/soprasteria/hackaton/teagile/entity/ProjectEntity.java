package com.soprasteria.hackaton.teagile.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for Project
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@NoArgsConstructor
@Data
@Table(name = "project")
public class ProjectEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "status", nullable = false)
	private String status;

	@OneToMany(mappedBy = "project")
	private List<TaskEntity> tasks;

	@OneToMany(mappedBy = "project")
	private List<MeetingEntity> meetings;
}
