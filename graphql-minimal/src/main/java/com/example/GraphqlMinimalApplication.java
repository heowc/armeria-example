package com.example;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.graphql.GraphqlService;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

public class GraphqlMinimalApplication {

	public static void main(String[] args) throws URISyntaxException {
		ServerBuilder sb = Server.builder();
		Server server = sb.http(8080)
				.service("/graphql", GraphqlService.builder().runtimeWiring(c -> {
					c.type("Query",
							typeWiring -> typeWiring.dataFetcher("user", new UserDataFetcher()));
				}).build())
				.build();
		CompletableFuture<Void> future = server.start();
		future.join();
	}
}
