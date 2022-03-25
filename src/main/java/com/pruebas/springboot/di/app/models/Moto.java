package com.pruebas.springboot.di.app.models;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

public class Moto {

    private static final long serialVersionUID = 1L;
    @NotNull
    private int idmoto;
    @NotNull
    private int idproveedor;
    @NotNull
    private int monto;
    

    public Moto() {
    }


	public Moto(@NotNull int idmoto, @NotNull int idproveedor, @NotNull int monto) {
		super();
		this.idmoto = idmoto;
		this.idproveedor = idproveedor;
		this.monto = monto;
	}


	public int getIdmoto() {
		return idmoto;
	}


	public void setIdmoto(int idmoto) {
		this.idmoto = idmoto;
	}


	public int getIdproveedor() {
		return idproveedor;
	}


	public void setIdproveedor(int idproveedor) {
		this.idproveedor = idproveedor;
	}


	public int getMonto() {
		return monto;
	}


	public void setMonto(int monto) {
		this.monto = monto;
	}


	@Override
	public String toString() {
		return "Moto [idmoto=" + idmoto + ", idproveedor=" + idproveedor + ", monto=" + monto + "]";
	}	
}
