package com.kyouhei.mathapp.entity;



import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "problems")
@Data
@ToString(exclude = "choices")
public class Problem{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "category_id")
	private int categoryId;
	
	@Lob
	private String question;
	
	private String hint;
	
	@OneToMany(mappedBy = "problem",cascade = CascadeType.ALL)
			//problems側に外部キーなし
	private List<Choice> choices;

}
