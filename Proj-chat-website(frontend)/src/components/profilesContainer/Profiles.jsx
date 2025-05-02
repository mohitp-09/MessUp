import React, { useState } from 'react';
import './profiles.css';
import { EllipsisVertical, Search } from 'lucide-react';

function Profiles({ setMessageVisible }) {
  const handleProfileClick = () => {
    setMessageVisible(true); // Show the message container when a profile is clicked
  };

  return (
    <div className='profile-main-container'>
      <div className='user-main'>
        <div className='top'>
          <p className="brand-title">MessUp</p>
          <EllipsisVertical />
        </div>

        <div className='profile-container'>
          <div className='searchBar'>
            <Search />
            <input type="text" placeholder="Search" />
          </div>

          <div className='labels'>
            <span className='label'>Group</span>
            <span className='label'>Unread</span>
          </div>
        </div>
      </div>

      <div className='profile-chats'>
        <div className="chat-item" onClick={handleProfileClick}>
          <div className="chat-img">
            <img className='profile-image' src="https://i.pravatar.cc/40?img=1" alt="user" />
            <div className="online-dot"></div>
          </div>
          <div className="chat-details">
            <div className="chat-top">
              <span className="chat-name">Ketan</span>
              <span className="chat-time">08:43</span>
            </div>
            <div className="chat-msg">Bhai monty, vardhan chutiya h!!</div>
          </div>
        </div>

        <div className="chat-item" onClick={handleProfileClick}>
          <div className="chat-img">
            <img className='profile-image' src="https://i.pravatar.cc/40?img=1" alt="user" />
            {/* <div className="online-dot"></div> */}
          </div>
          <div className="chat-details">
            <div className="chat-top">
              <span className="chat-name">Ketan</span>
              <span className="chat-time">08:43</span>
            </div>
            <div className="chat-msg">Bhai monty, vardhan chutiya h!!</div>
          </div>
        </div>

      </div>
      
    </div>
  );
}

export default Profiles;
