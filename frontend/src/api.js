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

export const createPost = async ({ authorID, title, content, allowComments }) => {
  console.log("createPost: ", content );
    const response = await axios.post('http://localhost:8080/createPost', {
      authorID,
      title,
      content,
      allowComments,
    });
    return response.data;
};

//Comments
export const getComments = async ({ postID, commentIDStart, count, reverse }) => {
    try {
      const response = await axios.get('http://localhost:8080/getComments', {
        params: {
          postID,
          commentIDStart,
          count,
          reverse,
        },
      });
      return response.data;
    } catch (error) {
      throw new Error(error.message);
    }
};

export const getComment = async ({postID, commentID}) => {
    //console.log("getReq: ", postID);
    const response = await axios.get('http://localhost:8080/getComment', {
      params: { postID, commentID },
    });
    return response.data;
};

export const createComment = async ({ postID, authorID, content }) => {
    console.log("createComment: ", postID, authorID, content);
    const response = await axios.post('http://localhost:8080/createComment', {
      postID,
      authorID,
      content,
    });
    return response.data;
};

export const upvotePost = async ({postID}) => {
  console.log("upvotePost: ", postID);
  try {
    const response = await axios.put('http://localhost:8080/upvotePost', JSON.stringify({ postID }), {
      headers: { 'Content-Type': 'application/json' },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const downvotePost = async ( {postID} ) => {
  try {
    const response = await axios.put('http://localhost:8080/downvotePost', JSON.stringify({ postID: postID }), {
      headers: { 'Content-Type': 'application/json' },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const upvoteComment = async ({ postID, commentID }) => {
  try {
    console.log("upvoteComment: ", postID, commentID);
    const response = await axios.put('http://localhost:8080/upvoteComment', JSON.stringify({ postID: postID, commentID: commentID }), {
      headers: { 'Content-Type': 'application/json' },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const downvoteComment = async ({ postID, commentID }) => {
  try {
    const response = await axios.put('http://localhost:8080/downvoteComment', JSON.stringify({ postID: postID, commentID: commentID }), {
      headers: { 'Content-Type': 'application/json' },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};