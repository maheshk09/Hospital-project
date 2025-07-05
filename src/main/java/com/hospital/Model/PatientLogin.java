package com.hospital.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "patient_login")
public class PatientLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//change done
    private Long id;
    @Column(unique = true)
    private String email;//integer string
    private String password;
    private String name;
    private int age;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private PatientPersonal patientPersonal;

    
    
	public PatientLogin(Long id, String email, String password,String name, int age, PatientPersonal patientPersonal) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.name=name;
		this.age=age;
		this.patientPersonal = patientPersonal;
	}

	
	public PatientLogin() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PatientPersonal getPatientPersonal() {
		return patientPersonal;
	}

	public void setPatientPersonal(PatientPersonal patientPersonal) {
		this.patientPersonal = patientPersonal;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}

    
}
