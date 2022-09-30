package no.hvl.dat250.rest.todos;

import com.google.gson.Gson;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

/**
 * Rest-Endpoint.
 */

public class TodoAPI {

    static HashMap<Long, Todo> todos = null;
    //static List<Todo> todos = null;


    public static void main(String[] args) {
        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        todos = new HashMap<Long, Todo>();
        //todos = new ArrayList<>();

        after((req, res) -> {
            res.type("application/json");
        });


        get("/todos/:id", (req, res) -> {
            Gson gson = new Gson();
            String idString = "notANumber";
            try{
                Long id = Long.parseLong(req.params(":id"));
                if (!todos.containsKey(id)){
                    return String.format("Todo with the id  \"%s\" not found!", id);
                }
                return todos.get(id).toJson();}
            catch(Exception e) {
                return (String.format("The id \"%s\" is not a number!", req.params(":id")));

            }


        });

        get("/todos", (req, res) -> {
            List<Todo> list = new ArrayList<Todo>(todos.values());;
            Gson gson = new Gson();
            return gson.toJson(list);

        });

        put("/todos/:id", (req,res) -> {
            try {
                Gson gson = new Gson();
                Long id = Long.parseLong(req.params(":id"));
                Todo todo = todos.get(id);
                todo = gson.fromJson(req.body(), Todo.class);

                todo.setId(id);
                todos.replace(id, todo);
                return todo.toJson();
            }
            catch (Exception e){
                return (String.format("The id \"%s\" is not a number!", req.params(":id")));
            }

        });

        post("/todos", (req,res) -> {
            Gson gson = new Gson();
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todos.put(todo.getId(), todo);

            return todo.toJson();
        });


        delete("/todos/:id", (req,res) -> {
            Gson gson = new Gson();
            String idString = "notANumber";
            try {
                Long id = Long.parseLong(req.params("id"));
                if (!todos.containsKey(id)) {
                    return String.format("Todo with the id  \"%s\" not found!", id);
                }
                todos.remove(id);
                return todos.get(id).toJson();
            }
            catch (Exception e){
                return (String.format("The id \"%s\" is not a number!", req.params(":id")));
            }


        });


    }

}