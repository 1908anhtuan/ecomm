import React, { useContext } from 'react';
import { NavLink } from 'react-router-dom';
import { AuthContext } from './AuthContext';
import { useNavigate } from 'react-router-dom';
import jwt_decode from 'jwt-decode';

import './navbar.css';

function NavBar() {
  const { isLoggedIn, logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const accountName = sessionStorage.getItem('accountName');

  const token = sessionStorage.getItem('accessToken');
  const decodedToken = token ? jwt_decode(token) : null;
  const userRoles = decodedToken && decodedToken.roles ? decodedToken.roles : [];
  console.log(JSON.stringify(decodedToken))
  const handleLogout = () => {
    logout();
  };
  console.log(isLoggedIn);
  const links = [
    {
      id: 1,
      path: '/',
      text: 'Home Page',
    },
    {
      id: 4,
      path: '/alladspage',
      text: 'All featuring adverts',
    },
  ];

  if (isLoggedIn) {
    if (userRoles.includes('RegularUser') || userRoles.includes('Admin')) {
      links.push(
        {
          id: 9,
          path: '/buyhistory',
          text: 'Buy history',
        },
        {
          id: 10,
          path: '/allchat',
          text: 'Conversations',
        },{
          id:5,
          path: '/profile',
          text: 'Profile'
        },
        {
         id:12,
         path:'/placeadvert',
         text:'Place advert' 
        }

      );
    }
  
    if (userRoles.includes('Admin')) {
      links.push(
        {
          id: 11,
          path: '/verification',
          text: 'Advert Dashboard',
        },
        {
          id:13,
          path:'/delivery',
          text:'Delivery'
        }
      );
    }
  
    links.push(
      {
        id: 6,
        path: '/login',
        text: 'Logout',
        onClick: handleLogout,
        className: 'logout',
      }
    );
  } else {
    links.push(
      {
        id: 2,
        path: '/register',
        text: 'Register',
      },
      {
        id: 3,
        path: '/login',
        text: 'Login',
      }
    );
  }

  return (
    <nav className="navbar">
      <ul className="navbar-ul">
        {links.map((link) => {
          return (
            <li key={link.id}>
              <NavLink className={`navbar-ul-li-a ${link.className}`} to={link.path} onClick={link.onClick}>
                {link.text}
              </NavLink>
            </li>
          );
        })}
        {isLoggedIn && (
          <li>
            <span className="account-name">{accountName}</span>
          </li>
        )}
      </ul>
    </nav>
  );
}

export default NavBar;