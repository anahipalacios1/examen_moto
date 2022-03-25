package com.pruebas.springboot.di.app.models;


import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;


public class Alumno implements Serializable {

		//Declaracion de variables que tienen que estar escritos identicos a lo de la base de datos
		private static final long serialVersionUID = 1L;
		@NotNull
		private int idalumno;
		@NotEmpty
		private String nombre;
		@NotEmpty
		private String apellido;
		
		//Constructores
		public Alumno() {
		}


		public Alumno(@NotNull int idalumno, @NotEmpty String nombre, @NotEmpty String apellido) {
			super();
			this.idalumno = idalumno;
			this.nombre = nombre;
			this.apellido = apellido;
		}


		public int getIdalumno() {
			return idalumno;
		}


		public void setIdalumno(int idalumno) {
			this.idalumno = idalumno;
		}


		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getApellido() {
			return apellido;
		}

		public void setApellido(String apellido) {
			this.apellido = apellido;
		}

		@Override
		public String toString() {
			return "Alumno{" + "idalumno=" + idalumno + "   nombre=" + nombre + "   apellido=" + apellido + '}' + "\n";
		}
		
}
