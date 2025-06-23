import encryptionService from "./encryption.js"

async function testEncryption() {
  const output = document.getElementById("output");
  output.innerText = "Initializing...\n";

  await encryptionService.initialize();

  const contact = "demo-user";
  await encryptionService.exchangePublicKeys(contact);

  const message = "Hello secure world!";
  const encrypted = await encryptionService.encryptMessage(message, contact);
  output.innerText += `Encrypted:\n${encrypted}\n\n`;

  const decrypted = await encryptionService.decryptMessage(encrypted);
  output.innerText += `Decrypted:\n${decrypted}\n`;
}

// 🧠 Make the function available globally (only needed for inline `onclick`)
window.testEncryption = testEncryption;
