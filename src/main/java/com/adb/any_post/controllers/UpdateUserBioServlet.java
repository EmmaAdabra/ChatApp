package com.adb.any_post.controllers;

import com.adb.any_post.dao.userdao.UserDao;
import com.adb.any_post.dto.SessionUserDTO;
import com.adb.any_post.exceptions.InputValidationException;
import com.adb.any_post.exceptions.UnknownException;
import com.adb.any_post.services.UserService;
import com.adb.any_post.utils.Response;
import com.adb.any_post.utils.ResponseCode;
import com.adb.any_post.utils.ValidateInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "UpdateBioServlet", value = "/updateBio")
public class UpdateUserBioServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserBioServlet.class);
    private static final UserService userService = new UserService(new UserDao());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if(session != null && session.getAttribute("sessionUser") != null){
            SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("sessionUser");
            String userBio = request.getParameter("userBio");
            Response<Integer> serviceResponse;

            try {
                if(userBio == null || userBio.isEmpty()){
                    userBio = "";
                } else {
                    ValidateInputs.validateUserBio(userBio);
                }
                serviceResponse = userService.addBio(userBio, sessionUser.getUserId());
                if(serviceResponse.getStatus() == ResponseCode.RESOURCE_CREATED.getCode()){
                    response.getWriter().write(serviceResponse.getMessage());
                    sessionUser.setBio(userBio);
                    session.setAttribute("sessionUser", sessionUser);
                } else {
                    response.getWriter().write(serviceResponse.getMessage());
                }
            } catch (UnknownException e) {
                response.getWriter().write(e.getMessage());
                logger.error("An unknown error -- {}",e.getMessage(), e);
            } catch (InputValidationException e) {
                response.getWriter().write(e.getMessage());
            } catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        } else {
            response.getWriter().write("Session expired, refresh and login");
        }
    }
}
