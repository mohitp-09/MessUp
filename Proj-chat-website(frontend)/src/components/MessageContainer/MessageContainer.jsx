import React from "react";
import "./messageContainer.css";
import {
  Search,
  Video,
  Phone,
  Smile,
  SendHorizontal,
  AudioLines,
  Camera,
  CheckCheck,
} from "lucide-react";
import profileImg from "../../assets/img.jpg";

function MessageContainer() {
  return (
    <div className="message-container">
      {/* Header */}
      <div className="user-profile-main">
        <div className="profile-chat">
          <img src={profileImg} alt="profile" className="profile-img" />
          <div className="left">
            <p className="profile-name">Harshvardhan</p>
            <div className="profile-status">
              <span className="online-dot-chat"></span> Online
            </div>
          </div>
          <div className="right">
            <Search />
            <Video />
            <Phone />
          </div>
        </div>
      </div>

      {/* Chat section */}
      <div className="chats">
        <div className="message right-message first-msg">
          <p>Hey! How's it going?</p>
          <span className="meta">
            10:06 pm <CheckCheck size={14} className="tick" />
          </span>
        </div>
        <div className="message left-message first-msg">
          <p>All good! You?</p>
          <span className="meta">10:07 pm</span>
        </div>
        <div className="message right-message">
          <p>Working on the new UI 💻</p>
          <span className="meta">
            10:08 pm <CheckCheck size={14} className="tick" />
          </span>
        </div>
        <div className="message left-message">
          <p>Nice! Send a preview!</p>
          <span className="meta">10:09 pm</span>
        </div>
      </div>

      {/* Typing section */}
      <div className="typing-section">
        <button className="emoji-btn">
          <Smile />
        </button>
        <input type="text" className="chat-input" placeholder="Type a message" />
        <button className="camera-btn">
          <Camera />
        </button>
        <button className="voice-btn">
          <AudioLines />
        </button>
        <button className="send-btn">
          <SendHorizontal />
        </button>
      </div>
    </div>
  );
}

export default MessageContainer;
