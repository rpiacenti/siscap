package br.com.correios.ppjsiscap.enums;

public enum UsuarioDoGrupo {

	ADMINISTRADOR_TECNICO {
		@Override
		public String toString() {
			return "ADMINISTRADOR TECNICO";
		}
	},
	ADMINISTRADOR_DE_SISTEMA {
		@Override
		public String toString() {
			return "ADMINISTRADOR DE SISTEMA";
		}
	},
	USUARIO_PPJ {
		@Override
		public String toString() {
			return "USUÁRIO PPJ";
		}
	};

}
