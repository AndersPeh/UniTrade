import React from "react";

// Unauthorized Component
// This component will display an unauthorized access message
// It will inform the user that they do not have permission to access the page
const Unauthorized = () => {
  return (
    <div className='unauthorized'>
      <h1>Unauthorized</h1>
      <p>You do not have permission to access this page.</p>
    </div>
  );
};

export default Unauthorized;
