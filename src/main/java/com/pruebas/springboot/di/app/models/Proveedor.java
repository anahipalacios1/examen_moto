package com.pruebas.springboot.di.app.models;


import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;


public class Proveedor implements Serializable {

		//Declaracion de variables que tienen que estar escritos identicos a lo de la base de datos
		private static final long serialVersionUID = 1L;
		@NotNull
		private int idproveedor;
		@NotEmpty
		private String nombre;
		
		//Constructores
		public Proveedor() {
		}


		public Proveedor(@NotNull int idproveedor, @NotEmpty String nombre) {
			super();
			this.idproveedor = idproveedor;
			this.nombre = nombre;
		}


		public int getIdproveedor() {
			return idproveedor;
		}


		public void setIdproveedor(int idproveedor) {
			this.idproveedor = idproveedor;
		}


		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}


		@Override
		public String toString() {
			return "Proveedor [idproveedor=" + idproveedor + ", nombre=" + nombre + "]";
		}

		
}
