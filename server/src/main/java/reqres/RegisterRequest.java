package reqres;

public record RegisterRequest(
        String username,
        String password,
        String email){
}
