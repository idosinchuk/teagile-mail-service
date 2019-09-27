package com.soprasteria.hackaton.teagile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for Meeting
 * 
 * @author Igor Dosinchuk
 * @author Luis Rapestre
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@NoArgsConstructor
@Data
@Table(name = "meeting")
public class MeetingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "meeting_id")
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

	@Column(name = "label_color")
	private String labelColor;

	@Column(name = "status")
	private String status;

	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private ProjectEntity project;
}
