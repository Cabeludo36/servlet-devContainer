package com.models.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class DatabaseConnection {
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=public&user=postgres&password=Postgres2022!";

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    public static String getUrl() {
        return URL;
    }
    

    /**
     * Estabelece uma conexão com o banco de dados usando as credenciais fornecidas.
     * Essa conexão é usada para executar consultas e comandos SQL.
     * A conexão deve ser fechada após o uso para evitar vazamentos de recursos.
     * @return Optional<Connection> - retorna uma conexão com o banco de dados ou vazio se não for possível estabelecer a conexão.
     */
    private static Optional<Connection> getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return Optional.of(DriverManager.getConnection(URL));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Obtém os nomes dos campos do tipo especificado.
     * @param returnType o tipo de objeto que será retornado
     * @return um array de strings contendo os nomes dos campos do tipo especificado
     */
    private static String[] getReturnTypeFields(Class<?> returnType) {
        // Obtém os nomes dos campos do tipo especificado
        // Isso é usado para mapear os resultados da consulta para os campos do objeto
        Field[] fields = returnType.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * Busca quais campos existem no ResultSet e no tipo especificado.
     * Isso é usado para garantir que apenas os campos que existem no ResultSet e no tipo especificado sejam mapeados.
     * Isso é importante para evitar erros de reflexão ao tentar acessar campos que não existem no ResultSet ou no tipo especificado.
     * @param returnType o tipo de objeto que será retornado
     * @param resultSetMetadata os metadados do ResultSet
     * @return um array de strings contendo os nomes dos campos que existem no ResultSet e no tipo especificado
     * @throws SQLException
     */
    private static String[] getResultSetAllowedFields(Class<?> returnType, ResultSetMetaData resultSetMetadata) throws SQLException {
        // busca quais campos existem do ResultSet
        String[] returnTypeFields = getReturnTypeFields(returnType);
        var columnCount = resultSetMetadata.getColumnCount();
        List<String> columnNamesDict = new java.util.ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            if(Arrays.stream(returnTypeFields).anyMatch(resultSetMetadata.getColumnName(i + 1)::equals))
            {
                columnNamesDict.add(resultSetMetadata.getColumnName(i + 1));
            }
        }

        return columnNamesDict.toArray(new String[0]);
    }


    /**
     * Executa uma consulta SQL e retorna uma lista de objetos do tipo especificado.
     * O método utiliza reflexão para mapear os resultados da consulta para instâncias do tipo especificado.
     * @param query a consulta SQL a ser executada
     * @param returnType o tipo de objeto que será retornado
     * @param params os parâmetros a serem passados para a consulta (deve corresponder à ordem dos placeholders na consulta)
     * @return uma lista de objetos do tipo especificado
     */
    public static <T> List<T> runQueryForList(String query, Class<T> returnType, Object... params) {
        // T representa o tipo genérico do objeto que será retornado

        // inicializa a conexão como null
        Connection conn = null;

        // lista para armazenar os resultados
        List<T> results = new java.util.ArrayList<>();

        try {
            // obtém a conexão do banco de dados
            // se a conexão for bem-sucedida, atribui à variável conn
            // se não for possível estabelecer a conexão, lança uma exceção
            Optional<Connection> optionalConnection = getConnection();
            if (optionalConnection.isPresent()) {
                conn = optionalConnection.get();
            } else {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            // prepara a consulta SQL com os parâmetros fornecidos
            // utiliza PreparedStatement para evitar SQL Injection
            // e para facilitar a substituição dos placeholders (?) pelos valores reais
            // os parâmetros devem corresponder à ordem dos placeholders na consulta
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            // executa a consulta e obtém o resultado em um ResultSet
            var resultSet = stmt.executeQuery();

            // busca quais campos existem em comum entre o ResultSet e o tipo especificado
            String[] columnNames = getResultSetAllowedFields(returnType, resultSet.getMetaData());

            // cria um hashmap para mapear os nomes das colunas do ResultSet para seus índices
            // isso é útil para acessar os valores do ResultSet de forma eficiente
            // o hashmap é usado para evitar a necessidade de iterar sobre os nomes das colunas
            // a cada vez que um valor é acessado, melhorando o desempenho
            Map<String, Integer> columnNamesMap = new HashMap<>();
            for (String column : columnNames) {
                columnNamesMap.put(column, resultSet.findColumn(column));
            }

            // itera pelos valores do ResultSet
            while (resultSet.next()) {
                // para cada linha do ResultSet, cria uma nova instância do tipo especificado
                var instance = returnType.getDeclaredConstructor().newInstance();

                // itera pelos campos em comum entre o ResultSet e o tipo especificado
                for (String fieldName : columnNamesMap.keySet()) {
                    // usa reflexão para acessar o campo correspondente no objeto
                    var field = returnType.getDeclaredField(fieldName);
                    // define o campo como acessível, mesmo que seja privado
                    // isso é necessário para poder atribuir valores a campos privados
                    field.setAccessible(true);
                    // atribui o valor do ResultSet ao campo do objeto
                    // usa o método getObject do ResultSet para obter o valor da coluna correspondente
                    // o método getObject permite obter o valor de qualquer tipo de coluna
                    // e converte automaticamente para o tipo do campo do objeto do campo
                    field.set(instance, resultSet.getObject(columnNamesMap.get(fieldName)));
                }

                results.add(instance);  
            }

            return results;
            
        } catch (Exception e) {
            // captura exceções específicas de SQL
            // isso é útil para identificar problemas com a consulta ou com a conexão
            e.printStackTrace();
        } finally {
            // garante que a conexão seja fechada após o uso
            // isso é importante para evitar vazamentos de recursos
            // se a conexão for nula, não faz nada
            // se a conexão não for nula, tenta fechá-la
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // retorna a lista de resultados
        return results;
    }


    /**
     * @param query a consulta SQL a ser executada
     * @param typeConstructor o tipo de objeto que será retornado
     * @param params os parâmetros a serem passados para a consulta (deve corresponder à ordem dos placeholders na consulta)
     * @return um objeto do tipo especificado ou null se não houver resultados
     */
    public static <T> T runQuery(String query, Class<T> typeConstructor, Object... params) {
        // T representa o tipo genérico do objeto que será retornado

        // chama o método runQueryForList para obter uma lista de resultados
        // e retorna o primeiro elemento da lista ou null se a lista estiver vazia
        // isso é útil quando se espera apenas um único resultado da consulta
        // por exemplo, ao buscar um único registro pelo ID
        return runQueryForList(query, typeConstructor, params).stream().findFirst().orElse(null);
    }

    /**
     * @param command a instrução SQL a ser executada (INSERT, UPDATE, DELETE)
     * @param params os parâmetros a serem passados para a instrução (deve corresponder à ordem dos placeholders na instrução)
     * @return o número de linhas afetadas pela instrução
     */
    public static long runCommand(String command, Object... params) {
        // inicializa a conexão como null
        Connection conn = null;
        // variável para armazenar o número de linhas afetadas pela instrução
        // é inicializada com 0, pois se não houver linhas afetadas, o valor será 0
        // isso é útil para verificar se a instrução foi executada com sucesso
        long results = 0;
        try {
            
            // obtém a conexão do banco de dados
            // se a conexão for bem-sucedida, atribui à variável conn
            // se não for possível estabelecer a conexão, lança uma exceção
            Optional<Connection> optionalConnection = getConnection();
            if (optionalConnection.isPresent()) {
                conn = optionalConnection.get();
            } else {
                throw new RuntimeException("Failed to establish a database connection.");
            }


            // prepara a consulta SQL com os parâmetros fornecidos
            // utiliza PreparedStatement para evitar SQL Injection
            // e para facilitar a substituição dos placeholders (?) pelos valores reais
            // os parâmetros devem corresponder à ordem dos placeholders na consulta
            PreparedStatement stmt = conn.prepareStatement(command);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            // executa a instrução SQL e obtém o número de linhas afetadas
            // o método executeUpdate é usado para instruções que modificam os dados
            // como INSERT, UPDATE ou DELETE
            // retorna o número de linhas afetadas pela instrução
            results = stmt.executeUpdate();
            return results;

        } catch (Exception e) {
            // captura qualquer exceção que ocorra durante a execução da consulta
            // e imprime o stack trace para depuração
            e.printStackTrace();
        } finally {
            // garante que a conexão seja fechada após o uso
            // isso é importante para evitar vazamentos de recursos
            // se a conexão for nula, não faz nada
            // se a conexão não for nula, tenta fechá-la
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // retorna a lista de resultados
        return results;
    }
}
