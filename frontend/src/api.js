// api.js
import axios from 'axios';

export const getPosts = async ({ postIDStart, count, reverse }) => {
  try {
    const response = await axios.get('http://localhost:8080/getPosts', {
      params: {
        postIDStart,
        count,
        reverse,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const getPost = async ({postID}) => {
    console.log("getReq: ", postID);
    const response = await axios.get('http://localhost:8080/getPost', {
      params: { postID },
    });
    return response.data;
  };
  
// api.js
export const createPost = async ({ authorID, title, content, allowComments }) => {
    const response = await axios.post('http://localhost:8080/createPost', {
      authorID,
      title,
      content,
      allowComments,
    });
    return response.data;
  };
  
