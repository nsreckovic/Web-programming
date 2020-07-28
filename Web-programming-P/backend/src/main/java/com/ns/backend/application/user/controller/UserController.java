package com.ns.backend.application.user.controller;

import com.ns.backend.application.user.models.UserType;
import com.ns.backend.application.user.models.request.UserLoginRequestDto;
import com.ns.backend.application.user.models.request.UserRegisterRequestDto;
import com.ns.backend.application.user.models.response.UserResponseDto;
import com.ns.backend.application.user.security.SecurityService;
import com.ns.backend.application.user.service.UserService;
import com.ns.backend.application.user.service.UserServiceImpl;
import com.ns.backend.utils.controller.ResponseMessageDto;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@RequiredArgsConstructor
public class UserController {

    private UserService userService = new UserServiceImpl();

    private SecurityService securityService = new SecurityService();

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(UserRegisterRequestDto user, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (!user.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();

        if (!userService.isUsernameAvailable(user.getUsername()))
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Username is already taken!")).build();
        if (!user.checkPasswords())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Passwords don't match!")).build();
        if (!user.validatePassword())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: The password must be longer than 6 characters and must contain both letters and numbers!")).build();

        UserResponseDto responseUser = userService.registerUser(user);
        if (responseUser != null)
            return Response.ok(new ResponseMessageDto("User registered successfully!")).build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserLoginRequestDto user) {
        if (!user.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();

        UserResponseDto responseUser = userService.loginUser(user);
        if (responseUser != null) {
            responseUser.setJwt(securityService.generateJWT(responseUser));
            return Response.ok(responseUser).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Bad username or password!")).build();
    }

    @POST
    @Path("/update/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("username") String current_username, UserRegisterRequestDto newUser, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (!userService.isUsernameAvailable(newUser.getUsername())) {
            if (!newUser.getUsername().equals(current_username))
                return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Username is already taken!")).build();
        }

        if (!newUser.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();
        if (!newUser.checkPasswords())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Passwords don't match!")).build();
        if (!newUser.validatePassword())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: The password must be longer than 6 characters and must contain both letters and numbers!")).build();

        UserResponseDto responseUser = userService.updateUser(current_username, newUser);
        if (responseUser != null) {
            responseUser.setJwt(securityService.generateJWT(responseUser));
            return Response.ok(responseUser).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/allCustomers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers(@HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<UserResponseDto> responseUsers = userService.getUsersByType(UserType.CUSTOMER);
        if (responseUsers != null) return Response.ok(responseUsers).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Error: No customers were found!")).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAdmins(@HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<UserResponseDto> responseUsers = userService.getAllUsers();
        if (responseUsers != null) return Response.ok(responseUsers).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Error: No admins were found!")).build();
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(@PathParam("username") String username, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        UserResponseDto responseUser = userService.getUserByUsername(username);
        if (responseUser != null) return Response.ok(responseUser).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/type/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersByType(@PathParam("type") String type, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();
        try {
            List<UserResponseDto> responseUsers = userService.getUsersByType(UserType.valueOf(type.toUpperCase()));
            if (responseUsers != null) return Response.ok(responseUsers).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/delete/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserByUsername(@PathParam("username") String username, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (userService.deleteUserByUsername(username)) return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
