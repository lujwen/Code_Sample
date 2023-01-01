import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import CartScreen from "../screens/CartScreen";

let mockIsAuthenticated = true;

const mockLoginWithRedirect = jest.fn();
const mockUseNavigate = jest.fn();

jest.mock("@auth0/auth0-react", () => ({
  ...jest.requireActual("@auth0/auth0-react"),
  Auth0Provider: ({ children }) => children,
  useAuth0: () => {
    return {
      isLoading: false,
      user: { sub: "foobar" },
      isAuthenticated: mockIsAuthenticated,
      loginWithRedirect: mockLoginWithRedirect,
    };
  },
}));

jest.mock("react-router-dom", () => ({
    ...jest.requireActual("react-router-dom"),
    useNavigate: () => {
      return mockUseNavigate;
    },
}));

test("renders ProfileDetail", () => {
    render(
        <MemoryRouter initialEntries={["/"]}>
        <CartScreen />
        </MemoryRouter>
    );
    expect(screen.getByText("Shopping Cart")).toBeInTheDocument();
    
    
});