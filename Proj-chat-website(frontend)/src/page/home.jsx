import React from 'react';
import Profiles from '../components/profilesContainer/profiles';
import MessageContainer from '../components/MessageContainer/messageContainer';
import '../App.css';

function Home() {
  return (
    <>
      <Profiles />
      <MessageContainer className="hideOnSmallScreen"/>
    </>
  );
}

export default Home;
