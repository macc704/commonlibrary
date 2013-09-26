package clib.preference.model;

import clib.common.model.CAbstractModelObject;

public abstract class CAbstractPreferenceCategory extends CAbstractModelObject
		implements ICPreferenceCategory {

	private static final long serialVersionUID = 1L;

	private CPreferenceRepository repository;

	public void setRepository(CPreferenceRepository repository) {
		this.repository = repository;
	}

	public CPreferenceRepository getRepository() {
		return repository;
	}

	@Override
	public String toString() {
		return getName();
	}

}
