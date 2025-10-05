import React from "react";
import { useSelector } from "react-redux";
import { useLocation, Navigate, Outlet } from "react-router-dom";

// ProtectedRoute Component
// This component will protect routes that require authentication and specific user roles
// It checks if the user is authenticated and has the required roles to access the route
// If not authenticated, it redirects to the login page
// If authenticated but not authorized, it redirects to the unauthorized page
const ProtectedRoute = ({ children, allowedRoles = [], useOutlet = false }) => {
  const location = useLocation();
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const userRoles = useSelector((state) => state.auth.roles);

  if (!isAuthenticated) {
    return <Navigate to='/login' state={{ from: location }} replace />;
  }

  const userRolesLower = userRoles.map((role) => role.toLowerCase());
  const allowedRolesLower = allowedRoles.map((role) => role.toLowerCase());

  const isAuthorized = userRolesLower.some((userRole) =>
    allowedRolesLower.includes(userRole)
  );

  if (isAuthorized) {
    return useOutlet ? <Outlet /> : children;
  } else {
    return <Navigate to='/unauthorized' state={{ from: location }} replace />;
  }
};

export default ProtectedRoute;
