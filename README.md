# progrmaJava


package usuario.gerenciar;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usuario.Usuario;
import usuario.SexoPessoa;

public class GerenciarUsuario {

	private Connection conn;
	private PreparedStatement pstm;
	private String sql;

	public GerenciarUsuario() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ifpr", "root", "root");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void salvar(Usuario usuario) {
		try {
			sql = "INSERT INTO tb_usuario (nome, email, sexo, data_nascimento) VALUES (?, ?, ?, ?)";
			pstm = conn.prepareStatement(sql);

			pstm.setString(1, usuario.getNome());
			pstm.setString(2, usuario.getEmail());
			pstm.setString(3, usuario.getSexo().toString());
			pstm.setDate(4, Date.valueOf(usuario.getDataNascimento()));
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void atualizar(Usuario usuario) {
		try {
			sql = "UPDATE tb_usuario SET nome = ?, email = ?, sexo = ?, data_nascimento = ? WHERE id = ?";
			pstm = conn.prepareStatement(sql);

			pstm.setString(1, usuario.getNome());
			pstm.setString(2, usuario.getEmail());
			pstm.setString(3, usuario.getSexo().toString());
			pstm.setDate(4, Date.valueOf(usuario.getDataNascimento()));
			pstm.setInt(5, usuario.getId());

			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Usuario> listar() {
		sql = "select * from tb_usuario order by id desc limit 20";
		List<Usuario> usuarios = null;
		try {
			pstm = conn.prepareStatement(sql);
			usuarios = executarSelect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usuarios;
	}

	private List<Usuario> executarSelect() throws SQLException {
		List<Usuario> usuarios = new ArrayList<>();
		ResultSet rs = pstm.executeQuery();
		while (rs.next()) {
			Usuario usuario = resultToObject(rs);
			usuarios.add(usuario);
		}
		return usuarios;
	}

	private Usuario resultToObject(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String nome = rs.getString("nome");
		String email = rs.getString("email");
		SexoPessoa sexo = SexoPessoa.valueOf(rs.getString("sexo"));
		Date dataNasc = rs.getDate("data_nascimento");

		Usuario usuario = new Usuario();
		usuario.setId(id);
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSexo(sexo);
		usuario.setDataNascimento(dataNasc.toLocalDate());
		return usuario;
	}

	public void remover(int id) {
		try {
			sql = "DELETE FROM tb_usuario WHERE id = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Usuario findById(int id) {
		Usuario usuario = null;
		try {
			sql = "SELECT * FROM tb_usuario WHERE id = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				usuario = resultToObject(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usuario;
	}

	public List<Usuario> pesquisarPorNome(String nome) {
		sql = "select * from tb_usuario where nome like concat(?, '%') order by id limit 20";
		List<Usuario> usuarios = null;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, nome);
			usuarios = executarSelect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usuarios;
	}

	public void fechar() {
		try {
			pstm.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
