// api.js

import axios from 'axios';
import { getAccessToken } from './redux/slices/auth';


const testURL = 'http://localhost:8080';
const prodURL = 'https://zenith-backend-azq7gdtpga-uc.a.run.app'; //to be filled 
const getApiUrl = () => {
  return process.env.NODE_ENV === 'production'
    ? prodURL
    : testURL;
};

const apiUrl = getApiUrl();
console.log("apiUrl: ", apiUrl);
export const getPosts = async ({ postIDStart, count, reverse }) => {
  try {
    const response = await axios.get(`${apiUrl}/post/gets`, {
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
    const response = await axios.get(`${apiUrl}/post/get`, {
      params: { postID },
    });
    return response.data;
};


export const createPost = async ({ title, content, allowComments }) => {
  const token = getAccessToken();
  console.log("createPost token: ", token);
  console.log("typeof token.credential: ", typeof token.credential);
  // const cred = (token.credential);
  // console.log("createPost credential: ", cred);
  const response = await axios.post(`${apiUrl}/post/create`, {
    title,
    content,
    allowComments,
  }, {
    headers: {
      Authorization: `Bearer ${token.credential}`,
      'X-Oauth-Provider': 'google',
      'X-Oauth-Credential': JSON.stringify(token.credential),
    },
  });
  return response.data;
};


export const editPost = async ({ postID, title, content, allowComments }) => {
  const token = getAccessToken();
  console.log("editPost token: ", token);
  console.log("typeof token.credential: ", typeof token.credential);
  // const cred = (token.credential);
  // console.log("editPost credential: ", cred);
  const response = await axios.put(`${apiUrl}/post/edit`, {
    postID,
    title,
    content,
    allowComments,
  }, {
    headers: {
      Authorization: `Bearer ${token.credential}`,
      'X-Oauth-Provider': 'google',
      'X-Oauth-Credential': JSON.stringify(token.credential),
    },
  });
  return response.data;
};




//Comments
export const getComments = async ({ postID, commentIDStart, count, reverse }) => {
    try {
      const response = await axios.get(`${apiUrl}/comment/gets`, {
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
    const response = await axios.get(`${apiUrl}/comment/get`, {
      params: { postID, commentID },
    });
    return response.data;
};

export const createComment = async ({ postID, content }) => {
  const token = getAccessToken();

  console.log("createComment: ", postID, content);
    const response = await axios.post(`${apiUrl}/createComment`, {
      postID,
      content,
    },{
      headers: {
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    return response.data;
};

export const upvotePost = async ({postID}) => {
  console.log("upvotePost: ", postID);
  try {
    const response = await axios.put(`${apiUrl}/post/upvote`, JSON.stringify({ postID }), {
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
    const response = await axios.put(`${apiUrl}/post/downvote`, JSON.stringify({ postID: postID }), {
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
    const response = await axios.put(`${apiUrl}/comment/upvote`, JSON.stringify({ postID: postID, commentID: commentID }), {
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
    const response = await axios.put(`${apiUrl}/comment/downvote`, JSON.stringify({ postID: postID, commentID: commentID }), {
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

export const deleteComment = async ({ postID, commentID }) => {
  const token = getAccessToken();
  try {
    const body = JSON.stringify({ postID, commentID });
    const response = await axios.delete(`${apiUrl}/comment/delete`, {
      data: body,
      headers: {
        'Content-Type': 'application/json' ,
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      }
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const editComment = async ({ postID, commentID, content }) => {
  const token = getAccessToken();
  try {
    const response = await axios.put(`${apiUrl}/comment/edit`, JSON.stringify({ postID: postID, commentID: commentID, content: content }), {
      headers:
          {
            'Content-Type': 'application/json' ,
              Authorization: `Bearer ${token.credential}`,
              'X-Oauth-Provider': 'google',
              'X-Oauth-Credential': JSON.stringify(token.credential),
          },
    });
    if (response.status !== 200) {
      throw new Error('Server Error');
    }
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

// User related API functions
export const getUsers = async ({ userIDStart, count, reverse }) => {
  const token = getAccessToken();

  try {
    const response = await axios.get(`${apiUrl}/user/gets`, {
      params: {
        userIDStart,
        count,
        reverse,
      }, headers:
      {
        'Content-Type': 'application/json' ,
          Authorization: `Bearer ${token.credential}`,
          'X-Oauth-Provider': 'google',
          'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const getUser = async (userID) => {
  try {
    const response = await axios.get(`${apiUrl}/user/get`, {
      params: { userID },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const createUser = async ({ name, email }) => {
  const token = getAccessToken();
  try {
    const response = await axios.post(`${apiUrl}/createUser`, {
      name,
      email,
    }, {
      headers: {
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const editUser = async ({ userID, name, email }) => {
  const token = getAccessToken();
  try {
    const response = await axios.put(`${apiUrl}/user/edit`, {
      userID,
      name,
      email,
    }, {
      headers: {
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};

export const promoteUser = async ({ target, reason }) => {
  const token = getAccessToken();
  try {
    const response = await axios.put(`${apiUrl}/user/edit`, {
      target,
      reason,
      
    }, {
      headers: {
        Authorization: `Bearer ${token.credential}`,
        'X-Oauth-Provider': 'google',
        'X-Oauth-Credential': JSON.stringify(token.credential),
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.message);
  }
};