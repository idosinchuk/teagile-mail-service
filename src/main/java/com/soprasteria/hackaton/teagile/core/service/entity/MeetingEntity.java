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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@NoArgsConstructor
@Data
@Table(name = "meeting")
public class MeetingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "meeting_id", updatable = false)
	private int id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "start_date", nullable = false)
	private String startDate;

	@Column(name = "end_date", nullable = false)
	private String endDate;

	@Column(name = "event_repeat_frequency", nullable = false)
	private int eventRepeatFrequency;

	@Column(name = "priority")
	private int priority;

	@Column(name = "status")
	private String status;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private ProjectEntity project;
}
