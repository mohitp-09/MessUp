import { useEffect, useRef, useState } from "react";
import ChatHeader from "./ChatHeader";
import MessageInput from "./MessageInput";
import { formatMessageTime, getDateLabel } from "../lib/utils";
import { useChatStore } from "../store/useChatStore";
import {
  MessageSquare,
  Sparkles,
  Check,
  CheckCheck,
  Shield,
  ShieldCheck,
  Lock,
} from "lucide-react";
import encryptionService from "../lib/encryption";

const ChatContainer = ({ selectedUser, onClose }) => {
  const messageEndRef = useRef(null);
  const { getMessagesForUser, sendMessage, currentUser } = useChatStore();

  const [messages, setMessages] = useState([]);
  const [hasEncryption, setHasEncryption] = useState(false);

  // Check if encryption is set up with this user
  useEffect(() => {
    if (selectedUser) {
      setHasEncryption(encryptionService.isInitialized);
    }
  }, [selectedUser]);

  // Load messages for selected user
  useEffect(() => {
    if (selectedUser) {
      const userMessages = getMessagesForUser(selectedUser.username);
      setMessages(userMessages);
    }
  }, [selectedUser, getMessagesForUser]);

  // Poll messages every second (replace with websocket later)
  useEffect(() => {
    if (!selectedUser) return;

    const interval = setInterval(() => {
      const userMessages = getMessagesForUser(selectedUser.username);
      setMessages(userMessages);
    }, 1000);

    return () => clearInterval(interval);
  }, [selectedUser, getMessagesForUser]);

  // Auto scroll when messages update
  useEffect(() => {
    if (messageEndRef.current) {
      messageEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

  const handleSendMessage = async (text, image) => {
    if (!text.trim() && !image) return;
    await sendMessage(text, image);
  };

  if (!selectedUser) return null;

  // Grouping helper (same sender, within 2 minutes)
  const isConsecutiveMessage = (currentMsg, prevMsg) => {
    if (!prevMsg || !currentMsg) return false;
    const isSameSender = currentMsg.senderId === prevMsg.senderId;
    const timeDiff =
      new Date(currentMsg.createdAt) - new Date(prevMsg.createdAt);
    return isSameSender && timeDiff < 2 * 60 * 1000;
  };

  // Simplified status display
  const getMessageStatusDisplay = (isOwnMessage, isDelivered) => {
    if (!isOwnMessage) return null;

    return isDelivered ? (
      <div className="flex items-center text-white/70">
        <CheckCheck className="size-3" />
      </div>
    ) : (
      <div className="flex items-center text-white/70">
        <Check className="size-3" />
      </div>
    );
  };

  return (
    <div className="flex-1 flex flex-col overflow-hidden bg-base-100">
      <ChatHeader user={selectedUser} onClose={onClose} />

      {/* Encryption Status Banner */}
      <div
        className={`px-4 py-2 border-b flex items-center justify-center gap-2 ${
          hasEncryption
            ? "bg-green-50 border-green-200"
            : "bg-yellow-50 border-yellow-200"
        }`}
      >
        {hasEncryption ? (
          <>
            <ShieldCheck className="size-4 text-green-600" />
            <span className="text-sm text-green-700 font-medium">
              End-to-end encrypted
            </span>
            <span className="text-xs text-green-600">
              Messages are secured with encryption
            </span>
          </>
        ) : (
          <>
            <Lock className="size-4 text-yellow-600" />
            <span className="text-sm text-yellow-700 font-medium">
              Setting up encryption...
            </span>
            <span className="text-xs text-yellow-600">
              Keys are being exchanged
            </span>
          </>
        )}
      </div>

      {/* Chat Body */}
      <div className="flex-1 overflow-y-auto p-4 space-y-1 bg-gradient-to-b from-base-100 to-base-50">
        {messages.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-16 text-center">
            <div className="flex justify-center gap-4 mb-8">
              <div className="relative">
                <div className="w-20 h-20 rounded-2xl bg-gradient-to-br from-primary/20 to-primary/10 flex items-center justify-center animate-bounce shadow-lg border border-primary/20 backdrop-blur-sm">
                  <MessageSquare className="w-10 h-10 text-primary" />
                </div>
                <div className="absolute -top-1 -right-1 w-6 h-6 rounded-full flex items-center justify-center">
                  <Shield className="w-3 h-3 text-white" />
                </div>
                <Sparkles className="absolute -top-2 -right-2 w-6 h-6 text-primary/60 animate-pulse" />
              </div>
            </div>

            <div className="space-y-6 max-w-sm">
              <h3 className="text-2xl font-bold text-base-content">
                Ready to chat securely with{" "}
                <span className="bg-gradient-to-r from-primary to-primary/80 bg-clip-text text-transparent">
                  {selectedUser.fullName}
                </span>
                ?
              </h3>
              <p className="text-base-content/60 leading-relaxed">
                Your messages are protected with end-to-end encryption. Start
                your secure conversation! 🔒✨
              </p>
            </div>
          </div>
        ) : (
          messages.map((message, index) => {
            const prevMessage = messages[index - 1];
            const nextMessage = messages[index + 1];
            const currDate = new Date(message.createdAt).toDateString();
            const prevDate = prevMessage
              ? new Date(prevMessage.createdAt).toDateString()
              : null;
            const showDateLabel = currDate !== prevDate;

            const isOwnMessage = message.senderId === currentUser?.username;
            const isConsecutive = isConsecutiveMessage(message, prevMessage);
            const isLastInGroup =
              !nextMessage || !isConsecutiveMessage(nextMessage, message);

            const messageStatus = getMessageStatusDisplay(
              isOwnMessage,
              message.isDelivered // <-- this should come from your backend/websocket
            );

            return (
              <div key={message._id}>
                {showDateLabel && (
                  <div className="flex items-center justify-center my-6">
                    <div className="bg-base-200/80 backdrop-blur-sm text-base-content/60 text-xs font-medium px-4 py-2 rounded-full shadow-sm">
                      {getDateLabel(message.createdAt)}
                    </div>
                  </div>
                )}

                <div
                  className={`flex ${
                    isOwnMessage ? "justify-end" : "justify-start"
                  } ${isConsecutive ? "mb-1" : "mb-3"}`}
                  ref={index === messages.length - 1 ? messageEndRef : null}
                >
                  {/* Avatar */}
                  {!isOwnMessage && !isConsecutive && (
                    <div className="mr-2 mt-auto">
                      <div className="size-8 rounded-full border border-base-300/50 shadow-sm overflow-hidden">
                        <img
                          src={selectedUser.profilePic}
                          alt="profile pic"
                          className="rounded-full object-cover"
                          onError={(e) => {
                            e.target.src = "/avatar.png";
                          }}
                        />
                      </div>
                    </div>
                  )}
                  {!isOwnMessage && isConsecutive && <div className="w-10"></div>}

                  {/* Message bubble */}
                  <div className="max-w-[70%] flex flex-col">
                    <div
                      className={`px-3 py-2 shadow-sm relative ${
                        isOwnMessage
                          ? "bg-gradient-to-br from-primary to-primary/90 text-primary-content ml-auto"
                          : "bg-base-200 text-base-content"
                      } ${
                        isOwnMessage
                          ? isConsecutive
                            ? isLastInGroup
                              ? "rounded-l-2xl rounded-tr-2xl rounded-br-md"
                              : "rounded-l-2xl rounded-r-md"
                            : isLastInGroup
                            ? "rounded-l-2xl rounded-tr-2xl rounded-br-md"
                            : "rounded-l-2xl rounded-tr-2xl rounded-br-sm"
                          : isConsecutive
                          ? isLastInGroup
                            ? "rounded-r-2xl rounded-tl-2xl rounded-bl-md"
                            : "rounded-r-2xl rounded-l-md"
                          : isLastInGroup
                          ? "rounded-r-2xl rounded-tl-2xl rounded-bl-md"
                          : "rounded-r-2xl rounded-tl-2xl rounded-bl-sm"
                      }`}
                    >
                      {message.image && (
                        <img
                          src={message.image}
                          alt="Attachment"
                          className="max-w-[200px] rounded-lg mb-2 shadow-sm"
                        />
                      )}

                      {message.text && (
                        <div className="flex items-end gap-2">
                          <p className="leading-relaxed flex-1">{message.text}</p>
                          <div className="flex items-center gap-1 text-xs flex-shrink-0 ml-2 mt-1">
                            <span className="text-[11px] opacity-70">
                              {formatMessageTime(message.createdAt)}
                            </span>
                            {messageStatus}
                          </div>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            );
          })
        )}
      </div>

      <MessageInput onSendMessage={handleSendMessage} />
    </div>
  );
};

export default ChatContainer;
