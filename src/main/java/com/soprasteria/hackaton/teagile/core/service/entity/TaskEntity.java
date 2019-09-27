package com.soprasteria.hackaton.teagile.core.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for Task
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@NoArgsConstructor
@Data
@Table(name = "task")
public class TaskEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
	private int id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "start_date", nullable = false)
	private String startDate;

	@Column(name = "end_date", nullable = false)
	private String endDate;

	@Column(name = "label_color")
	private String labelColor;

	@Column(name = "status")
	private String status;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private ProjectEntity project;
}
