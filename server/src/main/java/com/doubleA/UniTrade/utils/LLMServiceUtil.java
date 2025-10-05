package com.doubleA.UniTrade.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LLMServiceUtil {
  private final ChatModel chatModel;

  public String descriptionImage(MultipartFile image) throws IOException {
    // gets the MIME type of the image to tell the LLM what kind of file it is receiving.
    String mimeType = image.getContentType();
    if (mimeType == null || !mimeType.startsWith("image/")) {
      throw new IllegalArgumentException("Unsupported or missing image MIME type");
    }
    // converts the uploaded image to InputStreamResource for Spring to pass to other components.
    Resource resource = new InputStreamResource(image.getInputStream());
    // creates Chat client to interact with the LLM.
    return ChatClient.create(chatModel)
        // constructs new prompt to send to the LLM.
        .prompt()
        // tells LLM the following content is from the user (other roles such as system and
        // assistant).
        .user(
            // add text content as user's message.
            promptUserSpec ->
                promptUserSpec
                    .text(
                        """
                        Generate a concise, detailed textual description of the image strictly for visual similarity search. \
                        Please follow these rules:
                        - Limit the description to 2-3 short sentences or a list of key attributes.
                        - Focus ONLY on clearly visible, distinctive visual features such as:
                          * Colors, patterns, textures, shapes, and materials.
                          * Specific object types (e.g., 'red leather handbag', 'wooden dining table').
                          * Brand names or logos if clearly visible.
                          * Scene context ONLY if obvious (e.g., 'on a beach', 'in a kitchen').
                        - Do NOT include subjective opinions, guesses, or generic terms like 'product', 'item',
                         'electronics device', 'communication device', etc.
                        - Avoid filler words or vague language.
                        - Use simple, direct language suitable for automated similarity matching.
                        Provide the description in a clear, structured format (e.g., comma-separated attributes or bullet points).
                        """)
                    // add the image resource and mimetype of the image to the user message.
                    // so the request to the LLM is multimodal (with image and prompt text).
                    .media(MimeType.valueOf(mimeType), resource))
        // sends the complete prompt containing prompt text and image data to the LLM.
        .call()

        // After the LLM processes the request, extracts the text content from the response to
        // return it.
        .content();
  }
}
