package com.example;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;

class UserDataFetcher implements DataFetcher<UserDataFetcher.User> {

    private final Map<String, User> data = Map.of("1", new User("1", "hero"),
                                                  "2", new User("2", "human"),
                                                  "3", new User("3", "droid"));

    @Override
    public User get(DataFetchingEnvironment environment) throws Exception {
        final String id = environment.getArgument("id");
        return data.get(id);
    }

    static class User {
        private final String id;
        private final String name;

        User(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}