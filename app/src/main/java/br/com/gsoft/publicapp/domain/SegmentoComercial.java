package br.com.gsoft.publicapp.domain;

import java.io.Serializable;

@org.parceler.Parcel
public class SegmentoComercial {

	private static final long serialVersionUID = 1L;

	public Long id;

	public String nome;

	@Override
	public String toString() {
		return this.nome;
	}
}
